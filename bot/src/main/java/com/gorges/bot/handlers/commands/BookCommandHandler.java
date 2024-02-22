package com.gorges.bot.handlers.commands;

import com.gorges.bot.core.TelegramBot;
import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.services.BookConverterService;
import com.gorges.bot.services.MailService;
import com.gorges.bot.utils.ConvertResult;
import com.gorges.bot.utils.Utils;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.file.Path;
import java.util.List;

public class BookCommandHandler extends AbstractBookSender implements CommandHandler {

    public static final List<String> AVAILABLE_FORMATS = List.of(
        "fb2", "epub", "pdf"    // todo azw, azw3, kfx, etc...
    );

    private final BookConverterService bookConverterService;

    public BookCommandHandler(MailService mailService, UserRepository userRepository, BookConverterService bookConverterService) {
        super(mailService, userRepository);
        this.bookConverterService = bookConverterService;
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId, Object... args) throws TelegramApiException {
        Message message = update.getMessage();
        Document document = message.getDocument();

        String format = Utils.getFileExtension(document.getFileName());
        if (!AVAILABLE_FORMATS.contains(format)) {
            sendFormatErrorMessage(absSender, chatId);
            return;
        }

        final Message convertMessage;
        if (format.equals("pdf")) convertMessage = sendPdfMessage (absSender, chatId);
        else convertMessage = sendConvertMessage (absSender, chatId);

        final ConvertResult result = (book) -> {
            if (convertMessage != null) deleteMessage (absSender, convertMessage);
            Message sendingMessage = sendSendingMessage (absSender, chatId);
            sendBook(chatId, book);
            deleteMessage (absSender, sendingMessage);
            sendSentMessage (absSender, chatId);
        };

        bookConverterService.convert (
            downloadBook (absSender, document), result);
    }

    private Message sendConvertMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("✏ Конвертирую...")
            .build();
        return absSender.execute(sendMessage);
    }

    private java.io.File downloadBook (AbsSender absSender, Document document) throws TelegramApiException {
        GetFile getFile = GetFile.builder()
            .fileId(document.getFileId())
            .build();

        File file = absSender.execute(getFile);
        return TelegramBot.get().downloadFile(file, Path.of("" + document.getFileName()).toFile());
    }

    private void sendFormatErrorMessage (AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("❌ Неизвестный формат. Я могу читать только: "
                + String.join(", ", AVAILABLE_FORMATS) + ".")
            .build();

        absSender.execute(sendMessage);
    }

    private Message sendPdfMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("📝 Делаю pdf чуточку лучше...")
            .build();

        return absSender.execute(sendMessage);
    }

    @Override
    public Command getCommand() {
        return Command.BOOK;
    }
}
