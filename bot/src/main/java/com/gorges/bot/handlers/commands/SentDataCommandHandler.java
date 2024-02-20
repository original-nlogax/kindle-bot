package com.gorges.bot.handlers.commands;

import com.gorges.bot.handlers.ActionHandler;
import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.Command;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DataCommandHandler implements ActionHandler, CommandHandler {
    @Override
    public boolean canHandleAction(Update update, String action) {
        return  update.hasMessage() && (
                update.getMessage().hasDocument() ||
                update.getMessage().isReply());
    }

    @Override
    public void handleAction(AbsSender absSender, Update update, String action) throws TelegramApiException {

    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {

    }

    @Override
    public Command getCommand() {
        return Command.DATA;
    }
}
