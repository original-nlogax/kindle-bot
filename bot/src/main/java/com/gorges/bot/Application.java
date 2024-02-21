package com.gorges.bot;

import java.util.ArrayList;
import java.util.List;

import com.gorges.bot.handlers.commands.*;
import com.gorges.bot.handlers.commands.ForwardCommandHandler;
import com.gorges.bot.repositories.*;
import com.gorges.bot.repositories.database.AdminRepositoryDefault;
import com.gorges.bot.repositories.database.UserRepositoryDefault;
import com.gorges.bot.repositories.memory.MultiMessageRepositoryDefault;
import com.gorges.bot.services.MailService;
import com.gorges.bot.services.impl.MailServiceDefault;
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
    private MultiMessageRepository multiMessageRepository;

    private MessageService messageService;
    private MailService mailService;

    private CommandHandlerRegistry commandHandlerRegistry;
    private List<CommandHandler> commandHandlers;
    private List<UpdateHandler> updateHandlers;
    private List<ActionHandler> actionHandlers;

    private void initializeRepositories() {
        userActionRepository = new UserActionRepositoryDefault();
        userCommandStateRepository = new UserCommandStateRepositoryDefault();
        adminRepository = new AdminRepositoryDefault();
        userRepository = new UserRepositoryDefault();
        multiMessageRepository = new MultiMessageRepositoryDefault();
    }

    private void initializeServices() {
        messageService = new MessageServiceDefault();
        mailService = new MailServiceDefault(config);
    }

    private void initializeCommandHandlers() {
        commandHandlerRegistry = new CommandHandlerRegistryDefault();
        commandHandlers = new ArrayList<>();

        commandHandlers.addAll(List.of(
            new SentDataCommandHandler(commandHandlerRegistry, userActionRepository, multiMessageRepository),
            new EmailEnterCommandHandler(commandHandlerRegistry, userActionRepository, userRepository),
            new ForwardCommandHandler(multiMessageRepository, mailService, userRepository),
            new BookCommandHandler(mailService, userRepository)
        ));

        commandHandlerRegistry.setCommandHandlers(commandHandlers);
    }

    private void initializeUpdateHandlers() {
        updateHandlers = new ArrayList<>();

        updateHandlers.addAll(List.of(
            new StartCommandHandler(messageService, config, adminRepository, userRepository, commandHandlerRegistry)
        ));
    }

    private void initializeActionHandlers() {
        actionHandlers = new ArrayList<>();

        actionHandlers.addAll(List.of(
            new EmailEnterCommandHandler(commandHandlerRegistry, userActionRepository, userRepository),
            new SentDataCommandHandler(commandHandlerRegistry, userActionRepository, multiMessageRepository)
        ));
    }

    public static void main(String[] args) throws TelegramApiException {
        Application application = new Application();
        application.initializeRepositories();
        application.initializeServices();
        application.initializeCommandHandlers();
        application.initializeUpdateHandlers();
        application.initializeActionHandlers();

        TelegramBot.initialize(application.config, application.userActionRepository,
            application.updateHandlers, application.actionHandlers);

        new TelegramBotsApi(DefaultBotSession.class).registerBot(TelegramBot.get());
    }

}
