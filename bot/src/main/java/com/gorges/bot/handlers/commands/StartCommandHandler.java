package com.gorges.bot.handlers.commands;


import com.gorges.bot.core.Config;
import com.gorges.bot.handlers.UpdateHandler;
import com.gorges.bot.handlers.commands.registries.CommandHandlerRegistry;
import com.gorges.bot.models.domain.Button;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.models.entities.User;
import com.gorges.bot.repositories.AdminRepository;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.services.MessageService;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;

public class StartCommandHandler implements UpdateHandler {

    private final MessageService messageService;
    private final Config config;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final CommandHandlerRegistry commandHandlerRegistry;

    public StartCommandHandler(
        MessageService messageService, Config config,
        AdminRepository adminRepository, UserRepository userRepository, CommandHandlerRegistry commandHandlerRegistry) {
        this.config = config;
        this.messageService = messageService;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.commandHandlerRegistry = commandHandlerRegistry;
    }

    @Override
    public Command getCommand() {
        return Command.START;
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().startsWith(Button.START.getAlias());
    }

    @Override
    public void handleUpdate(AbsSender absSender, Update update) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();

        sendGreeting(absSender, chatId);

        User user = userRepository.findByChatId(chatId);

        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            saveUser(update);
            commandHandlerRegistry.find(Command.ENTER_EMAIL)
                .executeCommand(absSender, update, chatId);
        } else {
            commandHandlerRegistry.find(Command.SENT_DATA)
                .executeCommand(absSender, update, chatId, true);
        }
    }

    private void sendGreeting (AbsSender absSender, long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .text("👋")
            .replyMarkup(Button.createSettingsMenuKeyboard())
            .chatId(chatId)
            .build();
        absSender.execute(sendMessage);
    }

    private void saveUser (Update update) {
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();

        if (userRepository.findByChatId(chatId) != null)
            return;

        User user = new User();
        user.setChatId(chatId);
        user.setUsername(username);

        userRepository.save(user);
    }

    public void sendAdminPanelButton(AbsSender absSender, Long chatId) throws TelegramApiException {
        if (!adminRepository.isAdmin(chatId))
            return;

        String text = "Админ-Панель";
        String url = config.get("server.url");

        SetChatMenuButton chatMenuButton = SetChatMenuButton.builder()
            .chatId(chatId)
            .menuButton(MenuButtonWebApp.builder()
                .text(text)
                .webAppInfo(new WebAppInfo(url))
                .build())
            .build();

        absSender.execute(chatMenuButton);
    }
}
