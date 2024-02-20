package com.gorges.bot.handlers.commands;

import com.gorges.bot.handlers.ActionHandler;
import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.UserAction;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.repositories.UserActionRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Pattern;

public class ActionCommandHandler implements CommandHandler, ActionHandler {

    private final String ASK_NAME_ACTION = "person=name";
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]");
    private final UserActionRepository userActionRepository;

    public ActionCommandHandler(UserActionRepository userActionRepository) {
        this.userActionRepository = userActionRepository;
    }

    @Override
    public boolean canHandleAction(Update update, String action) {
        return update.hasMessage();
    }

    @Override
    public void handleAction(AbsSender absSender, Update update, String action) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        if (!NAME_PATTERN.matcher(text).find()) {
            // not correct name
            return;
        }

        // proceed
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        userActionRepository.updateByChatId(
            chatId, new UserAction(getCommand(), ASK_NAME_ACTION));

        SendMessage sendMessage = SendMessage.builder()
            .text("🧔 What's your name?")
            .chatId(update.getMessage().getChatId())
            .build();

        absSender.execute(sendMessage);
    }

    @Override
    public Command getCommand() {
        return Command.ENTER_NAME_EXAMPLE;
    }
}
