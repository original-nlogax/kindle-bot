package com.gorges.bot.handlers.commands;

import com.gorges.bot.core.TelegramBot;
import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.services.MailService;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


public class BookCommandHandler extends AbstractBookSender implements CommandHandler {

    public static final List<String> READABLE_FORMATS = List.of(
        "fb2", "epub", "pdf"    // todo azw, azw3, kfx, etc...
    );

    private final MailService mailService;
    private final UserRepository userRepository;

    public BookCommandHandler(MailService mailService, UserRepository userRepository) {
        super(mailService, userRepository);
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId, Object... args) throws TelegramApiException {
        Message message = update.getMessage();
        Document document = message.getDocument();
        deleteMessage(absSender, message);

        String format = document.getFileName().split("\\.")[1];
        if (!READABLE_FORMATS.contains(format)) {
            sendFormatErrorMessage(absSender, chatId);
            return;
        }

        Message processingMessage = sendProcessingMessage(absSender, chatId);
        java.io.File book = downloadBook (absSender, document);
        java.io.File convertedBook = convert (book);
        send (chatId, convertedBook);
        deleteMessage (absSender, processingMessage);
        sendSentMessage (absSender, chatId);
    }

    private java.io.File downloadBook (AbsSender absSender, Document document) throws TelegramApiException {
        GetFile getFile = GetFile.builder()
            .fileId(document.getFileId())
            .build();

        File file = absSender.execute(getFile);
        return TelegramBot.get().downloadFile(file);
    }

    private java.io.File convert (java.io.File book) {
        System.out.println(book.getAbsolutePath());
        return book;
    }

    private void convertToEpub () {

    }

    private void sendFormatErrorMessage (AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("Неизвестный формат! Я могу читать только "
                + String.join(", ", READABLE_FORMATS) + ".")
            .build();

        absSender.execute(sendMessage);
    }

    @Override
    public Command getCommand() {
        return Command.BOOK;
    }
}
