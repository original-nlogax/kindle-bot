package com.gorges.bot.handlers.commands;

import com.gorges.bot.handlers.ActionHandler;
import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.handlers.UpdateHandler;
import com.gorges.bot.handlers.commands.registries.CommandHandlerRegistry;
import com.gorges.bot.models.domain.Button;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.models.domain.UserAction;
import com.gorges.bot.models.entities.User;
import com.gorges.bot.repositories.UserActionRepository;
import com.gorges.bot.repositories.UserRepository;
import com.sanctionco.jmail.JMail;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class EmailEnterCommandHandler implements UpdateHandler, CommandHandler, ActionHandler {

    private final String ENTER_EMAIL_ACTION = "enter-email";

    private final CommandHandlerRegistry commandHandlerRegistry;
    private final UserActionRepository userActionRepository;
    private final UserRepository userRepository;

    public EmailEnterCommandHandler(CommandHandlerRegistry commandHandlerRegistry, UserActionRepository userActionRepository, UserRepository userRepository) {
        this.commandHandlerRegistry = commandHandlerRegistry;
        this.userActionRepository = userActionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean canHandleAction(Update update, String action) {
        return isAction(update, action, ENTER_EMAIL_ACTION);
    }

    @Override
    public void handleAction(AbsSender absSender, Update update, String action) throws TelegramApiException {
        long chatId = update.getMessage().getChatId();
        String email = update.getMessage().getText();

        if (!JMail.isValid(email)) {
            sendIncorrectEmailMessage(absSender, chatId);
            commandHandlerRegistry.find(Command.ENTER_EMAIL)
                .executeCommand(absSender, update, chatId);
            return;
        }

        saveEmail (email, chatId);
        sendReadyMessage (absSender, chatId);

        commandHandlerRegistry.find(Command.SENT_DATA)
            .executeCommand(absSender, update, chatId);
    }

    private void saveEmail (String email, long chatId) {
        User user = userRepository.findByChatId(chatId);
        user.setEmail(email);
        userRepository.update(user);
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId, Object... args) throws TelegramApiException {
        userActionRepository.updateByChatId(
            chatId, new UserAction(getCommand(), ENTER_EMAIL_ACTION));

        sendEmailEnterMessage(absSender, chatId);
    }

    private void sendReadyMessage(AbsSender absSender, long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("Отлично! Теперь вы можете пересылать сюда сообщения или книги")
            .replyMarkup(Button.createSettingsMenuKeyboard())
            .build();
        absSender.execute(sendMessage);
    }

    private void sendIncorrectEmailMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("❌ Email is incorrect")
            .build();
        absSender.execute(sendMessage);
    }

    private void sendEmailEnterMessage (AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("📧 Enter your kindle email:")
            .build();
        absSender.execute(sendMessage);
    }

    @Override
    public Command getCommand() {
        return Command.ENTER_EMAIL;
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage()
            && update.getMessage().getText().equals(Button.SETTINGS.getAlias());
    }

    @Override
    public void handleUpdate(AbsSender absSender, Update update) throws TelegramApiException {
        executeCommand(absSender, update, update.getMessage().getChatId());
    }
}
