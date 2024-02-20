package com.gorges.bot.handlers.commands;


import com.gorges.bot.core.Config;
import com.gorges.bot.handlers.UpdateHandler;
import com.gorges.bot.models.domain.Button;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.models.entities.User;
import com.gorges.bot.repositories.AdminRepository;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.services.MessageService;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartCommandHandler implements UpdateHandler {

    private final MessageService messageService;
    private final Config config;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public StartCommandHandler(
        MessageService messageService, Config config,
        AdminRepository adminRepository, UserRepository userRepository) {
        this.config = config;
        this.messageService = messageService;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
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
        SendMessage sendMessage = SendMessage.builder()
            .text(messageService.findByName("START_MESSAGE").buildText())
            .replyMarkup(Button.createGeneralMenuKeyboard())
            .chatId(chatId)
            .build();

        absSender.execute(sendMessage);

        saveUser(update);
        if (adminRepository.isAdmin(chatId)) {
            SendMessage sendMessage2 = SendMessage.builder()
                .text("admin")
                .chatId(chatId)
                .build();

            absSender.execute(sendMessage2);
        }

        if (adminRepository.isAdmin(chatId))
            sendAdminPanelButton(absSender, chatId);
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
        //if (!config.get("telegram.admin.chat-id").contains(String.valueOf(chatId)))
        //    return;

        if (!adminRepository.isAdmin(chatId))
            return;

        String text = "Админ-Панель";
        String url = config.get("server.url");

        /*
        SetWebhook setWebhook = SetWebhook.builder()
            .url(url)
            .certificate(new InputFile(
                StartCommandHandler.class.getClassLoader().getResourceAsStream("local-cert.crt"),
                "local-cert.crt"))
            .build();

        absSender.execute(setWebhook);*/

        /*DeleteWebhook deleteWebhook = DeleteWebhook.builder()
            .dropPendingUpdates(true)
            .build();

        absSender.execute(deleteWebhook);*/

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
