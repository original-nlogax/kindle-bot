package com.gorges.bot.handlers.commands;

import com.gorges.bot.models.entities.User;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.services.MailService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class AbstractBookSender {

    private final MailService mailService;
    private final UserRepository userRepository;

    protected AbstractBookSender (MailService mailService, UserRepository userRepository) {
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    protected void send(Long chatId, java.io.File book) {
        User user = userRepository.findByChatId(chatId);
        String to = user.getEmail();
        mailService.send(book, to);
    }

    protected Message sendProcessingMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("✉ Отправляю...")
            .build();

        return absSender.execute(sendMessage);
    }

    protected void sendSentMessage(AbsSender absSender, long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("✨ Отправлено!")
            .build();

        absSender.execute(sendMessage);
    }

    protected void deleteMessage(AbsSender absSender, Message message) throws TelegramApiException {
        DeleteMessage deleteMessage = DeleteMessage.builder()
            .messageId(message.getMessageId())
            .chatId(message.getChatId())
            .build();

        absSender.execute(deleteMessage);
    }

}
