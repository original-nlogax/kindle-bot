package com.gorges.bot.handlers.commands;

import com.gorges.bot.handlers.ActionHandler;
import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.handlers.commands.registries.CommandHandlerRegistry;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.models.domain.MultiMessage;
import com.gorges.bot.models.domain.UserAction;
import com.gorges.bot.repositories.MultiMessageRepository;
import com.gorges.bot.repositories.UserActionRepository;
import com.gorges.bot.utils.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SentDataCommandHandler implements ActionHandler, CommandHandler {

    private final String SENT_DATA_ACTION = "sent-data";
    private final CommandHandlerRegistry commandHandlerRegistry;
    private final UserActionRepository userActionRepository;
    private final MultiMessageRepository multiMessageRepository;

    public SentDataCommandHandler(CommandHandlerRegistry commandHandlerRegistry, UserActionRepository userActionRepository, MultiMessageRepository multiMessageRepository) {
        this.commandHandlerRegistry = commandHandlerRegistry;
        this.userActionRepository = userActionRepository;
        this.multiMessageRepository = multiMessageRepository;
    }

    @Override
    public boolean canHandleAction(Update update, String action) {
        return  update.hasMessage() && (
                update.getMessage().hasDocument() ||
                update.getMessage().isReply());
    }

    @Override
    public void handleAction(AbsSender absSender, Update update, String action) throws TelegramApiException {
        Message message = update.getMessage();

        if (message.hasDocument())
            document(absSender, update);

        else if (message.getForwardFromChat() != null &&
            message.getForwardFromChat().getType().equals("channel"))
            forward(absSender, update);

        else if (Utils.isValidURL(message.getText()))
            link(absSender, update);

        else sendPossibleInputsMessage(absSender, message.getChatId());
    }

    private void sendPossibleInputsMessage (AbsSender absSender, long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("Перешлите боту посты ✉, книги 📚 — или 🔗 ссылки на telegra.ph и teletype.in")
            .build();
        absSender.execute(sendMessage);
    }

    private void link (AbsSender absSender, Update update) throws TelegramApiException {
        commandHandlerRegistry.find(Command.LINK).executeCommand(
            absSender, update, update.getMessage().getChatId()
        );
    }

    private void document(AbsSender absSender, Update update) throws TelegramApiException {
        commandHandlerRegistry.find(Command.BOOK).executeCommand(
            absSender, update, update.getMessage().getChatId()
        );
    }

    private void forward(AbsSender absSender, Update update) {
        Long chatId = update.getMessage().getChatId();

        MultiMessage multiMessage = multiMessageRepository.getByChatId(chatId);

        if (multiMessage == null) {
            multiMessage = new MultiMessage();
            multiMessage.setCallback(() -> {
                try {
                    commandHandlerRegistry.find(Command.FORWARD).executeCommand(
                        absSender, update, chatId);
                    multiMessageRepository.delete(chatId);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        multiMessage.addMessage(update.getMessage());
        multiMessage.startCollecting(); // resetting timer after each new message

        multiMessageRepository.save(chatId, multiMessage);
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId, Object... args) throws TelegramApiException {
        userActionRepository.updateByChatId(
            chatId, new UserAction(getCommand(), SENT_DATA_ACTION));

        if (args.length > 0 && args[0] != null && (boolean) args[0]) {
            sendPossibleInputsMessage(absSender, chatId);
        }
    }

    @Override
    public Command getCommand() {
        return Command.SENT_DATA;
    }
}
