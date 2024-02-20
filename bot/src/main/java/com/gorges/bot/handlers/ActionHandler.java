package com.gorges.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface ActionHandler extends Handler {

    boolean canHandleAction(Update update, String action);

    void handleAction(AbsSender absSender, Update update, String action) throws TelegramApiException;

    default boolean isAction (Update update, String updateAction, String thisAction) {
        return update.hasMessage()
            && update.getMessage().hasText()
            && updateAction.equals(thisAction);
    }

}
