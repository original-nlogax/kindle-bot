package com.gorges.bot.handlers.commands;

import com.gorges.bot.models.entities.User;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.services.MailService;
import com.gorges.bot.utils.Utils;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractBookSender {

    public static final int MAX_TITLE_LENGTH = 32;

    private final MailService mailService;
    private final UserRepository userRepository;

    protected AbstractBookSender (MailService mailService, UserRepository userRepository) {
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    protected File createBook (String title, String author, String text) {
        String textHtml =
            "<html><body>Автор: " + author
            + "<br><br>" + (text.replace("\n", "<br>")) + "</body></html>";

        Book book = new Book();
        Metadata metadata = book.getMetadata();
        metadata.addTitle(title);
        metadata.addAuthor(new Author(author));
        book.setCoverImage(Utils.getResource("cover.png", "cover.png"));
        book.addSection("Text", new Resource(
            textHtml.getBytes(StandardCharsets.UTF_8), MediatypeService.XHTML));

        EpubWriter epubWriter = new EpubWriter();
        String filename = Utils.removeForbiddenFilenameCharacters(title) + ".epub";
        try {
            epubWriter.write(book, new FileOutputStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new File(filename);
    }

    protected void sendBook (Long chatId, java.io.File book) {
        User user = userRepository.findByChatId(chatId);
        String to = user.getEmail();
        mailService.send(book, to);
    }

    protected Message sendSendingMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("✉ Отправляю...")
            .build();
        return absSender.execute(sendMessage);
    }

    protected void sendSentMessage(AbsSender absSender, long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("✨ Отправлено! Книга появится в библиотеке через несколько минут")
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
