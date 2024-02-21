package com.gorges.bot.handlers.commands;

import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.models.domain.MultiMessage;
import com.gorges.bot.repositories.MultiMessageRepository;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.services.MailService;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ForwardCommandHandler extends AbstractBookProcessor implements CommandHandler {

    public static final String FORWARD_DELIMITER = "\n\n";
    public static final int MAX_TITLE_LENGTH = 40;

    private final MultiMessageRepository multiMessageRepository;
    private final MailService mailService;
    private final UserRepository userRepository;

    public ForwardCommandHandler(MultiMessageRepository multiMessageRepository, MailService mailService, UserRepository userRepository) {
        super(mailService, userRepository);
        this.multiMessageRepository = multiMessageRepository;
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId, Object... args) throws TelegramApiException {
        MultiMessage multiMessage = multiMessageRepository.getByChatId(chatId);
        String text = multiMessage.getMessages().stream()
            .map(Message::getText)
            .collect(Collectors.joining(FORWARD_DELIMITER));

        Message processingMessage = sendProcessingMessage (absSender, chatId);
        File book = createBook (multiMessage.getMessages().get(0), text);
        send (chatId, book);
        deleteMessage (absSender, processingMessage);
        sendSentMessage (absSender, chatId);
    }

    private File createBook(Message message, String text) {
        // todo redo \/\/\/\/
        String title = text.split("\n")[0];
        if (title.length() > MAX_TITLE_LENGTH)
            title = title.substring(MAX_TITLE_LENGTH);
        String fileTitle = title += ".pdf";
        title += "...";
        text = title + "\n\n" + text;
        // /\/\/\/\/\/\/\/\/\/\

        Book book = new Book();
        Metadata metadata = book.getMetadata();
        metadata.addTitle(title);
        metadata.addAuthor(new Author(message.getForwardSenderName(), ""));
        /*
        // todo channel picture cover image
        book.setCoverImage(
            getResource("/book1/test_cover.png", "cover.png") );*/
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        book.addSection(title, new Resource(bytes, MediatypeService.XHTML));
        EpubWriter epubWriter = new EpubWriter();

        try {
            epubWriter.write(book, new FileOutputStream(fileTitle));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new File(fileTitle);
    }

    @Override
    public Command getCommand() {
        return Command.FORWARD;
    }
}
