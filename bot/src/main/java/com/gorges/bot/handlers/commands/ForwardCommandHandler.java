package com.gorges.bot.handlers.commands;

import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.models.domain.MultiMessage;
import com.gorges.bot.repositories.MultiMessageRepository;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.services.MailService;
import com.gorges.bot.utils.Utils;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ForwardCommandHandler extends AbstractBookSender implements CommandHandler {

    public static final String MESSAGES_DELIMITER = "<br><br>";

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
            .map(this::getHtmlText)
            .collect(Collectors.joining(MESSAGES_DELIMITER));
        for (Message message : multiMessage.getMessages())
            deleteMessage (absSender, message);

        // todo multiple authors; pass multiMessage.getMessages() to createBook
        String author = multiMessage.getMessages().get(0).getForwardFromChat().getTitle();
        Message processingMessage = sendSendingMessage(absSender, chatId);
        File book = createBook (author, text);
        sendBook (chatId, book);
        deleteMessage (absSender, processingMessage);
        sendSentMessage (absSender, chatId);
    }

    private String getHtmlText (Message message) {
        if (!message.hasEntities())
            return message.getText();

        StringBuilder sb = new StringBuilder();
        sb.append(message.getText());

        int tagOffset = 0;
        for (MessageEntity entity : message.getEntities()) {
            String tag = switch (entity.getType()) {
                case "bold" -> "b";
                case "italic" -> "i";
                //case "url" -> "a href=" + entity.getUrl();  // todo getUrl is null
                default -> "";
            };
            String openingTag = "<" + tag + ">";
            String closingTag = "</" + tag + ">";
            sb.insert(entity.getOffset() + tagOffset, openingTag);
            tagOffset += openingTag.length();
            sb.insert(entity.getOffset() + entity.getLength() + tagOffset, closingTag);
            tagOffset += closingTag.length();
        }

        return sb.toString();
    }

    private File createBook(String author, String text) {
        String title = text.split("<br>")[0].replaceAll("<[^>]*>","");
        if (title.length() > MAX_TITLE_LENGTH)
            title = title.substring(0, MAX_TITLE_LENGTH);
        text = title + "<br><br>" + text.replace("\\\\r?\\\\n", "<br>");

        Book book = new Book();
        Metadata metadata = book.getMetadata();
        metadata.addTitle(title);
        metadata.addAuthor(new Author(author, author));
        book.setCoverImage(Utils.getResource("cover.png", "cover.png"));
        book.addSection("Text", new Resource(
            text.getBytes(StandardCharsets.UTF_8), MediatypeService.XHTML));

        String filename = Utils.removeForbiddenFilenameCharacters(title) + ".epub";
        EpubWriter epubWriter = new EpubWriter();

        try {
            epubWriter.write(book, new FileOutputStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new File(filename);
    }

    @Override
    public Command getCommand() {
        return Command.FORWARD;
    }
}
