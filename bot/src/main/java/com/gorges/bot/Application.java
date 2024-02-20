package com.gorges.bot;

import java.util.ArrayList;
import java.util.List;

import com.gorges.bot.handlers.commands.*;
import com.gorges.bot.repositories.AdminRepository;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.repositories.database.AdminRepositoryDefault;
import com.gorges.bot.repositories.database.UserRepositoryDefault;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.gorges.bot.core.Config;
import com.gorges.bot.core.TelegramBot;
import com.gorges.bot.handlers.ActionHandler;
import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.handlers.UpdateHandler;
import com.gorges.bot.handlers.commands.registries.CommandHandlerRegistry;
import com.gorges.bot.handlers.commands.registries.CommandHandlerRegistryDefault;
import com.gorges.bot.repositories.UserActionRepository;
import com.gorges.bot.repositories.UserCommandStateRepository;
import com.gorges.bot.repositories.memory.UserActionRepositoryDefault;
import com.gorges.bot.repositories.memory.UserCommandStateRepositoryDefault;
import com.gorges.bot.services.MessageService;
import com.gorges.bot.services.impl.MessageServiceDefault;

public class Application {

    private final Config config = Config.getInstance();

    private UserActionRepository userActionRepository;
    private UserCommandStateRepository userCommandStateRepository;
    private AdminRepository adminRepository;
    private UserRepository userRepository;

    private MessageService messageService;

    private CommandHandlerRegistry commandHandlerRegistry;
    private List<CommandHandler> commandHandlers;
    private List<UpdateHandler> updateHandlers;
    private List<ActionHandler> actionHandlers;

    private void initializeRepositories() {
        userActionRepository = new UserActionRepositoryDefault();
        userCommandStateRepository = new UserCommandStateRepositoryDefault();
        adminRepository = new AdminRepositoryDefault();
        userRepository = new UserRepositoryDefault();
    }

    private void initializeServices() {
        messageService = new MessageServiceDefault();
    }

    private void initializeCommandHandlers() {
        commandHandlerRegistry = new CommandHandlerRegistryDefault();
        commandHandlers = new ArrayList<>();

        commandHandlers.addAll(List.of(
            new ActionCommandHandler(userActionRepository)
        ));

        commandHandlerRegistry.setCommandHandlers(commandHandlers);
    }

    private void initializeUpdateHandlers() {
        updateHandlers = new ArrayList<>();

        updateHandlers.addAll(List.of(
            new StartCommandHandler(messageService, config, adminRepository, userRepository)
        ));
    }

    private void initializeActionHandlers() {
        actionHandlers = new ArrayList<>();

        actionHandlers.addAll(List.of(
            new ActionCommandHandler(userActionRepository)
        ));
    }

    public static void main(String[] args) throws TelegramApiException {
        Application application = new Application();
        application.initializeRepositories();
        application.initializeServices();
        application.initializeCommandHandlers();
        application.initializeUpdateHandlers();
        application.initializeActionHandlers();

        TelegramBot telegramBot = new TelegramBot(application.config, application.userActionRepository,
                application.updateHandlers, application.actionHandlers);

        new TelegramBotsApi(DefaultBotSession.class).registerBot(telegramBot);
    }

}
