package com.gorges.bot.core;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.gorges.bot.exceptions.HandlerNotFoundException;
import com.gorges.bot.handlers.ActionHandler;
import com.gorges.bot.handlers.UpdateHandler;
import com.gorges.bot.models.domain.UserAction;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.repositories.UserActionRepository;

public class TelegramBot extends TelegramLongPollingBot {

    private final Logger logger = LogManager.getLogger(getClass());

    private final String telegramBotUsername;
    private final UserActionRepository userActionRepository;
    private final Map<Command, UpdateHandler> updateHandlers;
    private final Map<Command, ActionHandler> actionHandlers;

    public TelegramBot(
            Config config,
            UserActionRepository userActionRepository,
            List<UpdateHandler> updateHandlers,
            List<ActionHandler> actionHandlers) {

        super(new DefaultBotOptions(), config.get("telegram.bot.token"));
        this.telegramBotUsername = config.get("telegram.bot.username");
        this.userActionRepository = userActionRepository;
        this.updateHandlers = updateHandlers.stream().collect(toMap(UpdateHandler::getCommand, identity()));
        this.actionHandlers = actionHandlers.stream().collect(toMap(ActionHandler::getCommand, identity()));

        System.out.println("Started");
    }

    @Override
    public String getBotUsername() {
        return telegramBotUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            handle(update);
        } catch (Exception e) {
            logger.error("Failed to handle update", e);
        }
    }

    private void handle(Update update) throws TelegramApiException {
        if (handleCommand(update)) {
            return;
        }
        if (handleAction(update)) {
            return;
        }
    }

    private boolean handleCommand(Update update) throws TelegramApiException {
        List<UpdateHandler> foundCommandHandlers = updateHandlers.values().stream()
                .filter(commandHandler -> commandHandler.canHandleUpdate(update))
                .toList();

        if (foundCommandHandlers.size() > 1) {
            throw new HandlerNotFoundException("Found more than one command handler: " + foundCommandHandlers.size());
        }
        if (foundCommandHandlers.size() != 1) {
            return false;
        }

        foundCommandHandlers.get(0).handleUpdate(this, update);
        return true;
    }

    private boolean handleAction(Update update) throws TelegramApiException {
        if (!update.hasMessage()) {
            return false;
        }

        UserAction userAction = userActionRepository.findByChatId(update.getMessage().getChatId());
        if (userAction == null) {
            return false;
        }

        ActionHandler actionHandler = actionHandlers.get(userAction.getCommand());
        if (actionHandler == null) {
            throw new HandlerNotFoundException("Failed to find action handler");
        }

        actionHandler.handleAction(this, update, userAction.getAction());
        return true;
    }

}
