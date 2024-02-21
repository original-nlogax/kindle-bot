package com.gorges.bot.handlers.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.models.domain.TelegraphArticle;
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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class LinkCommandHandler extends AbstractBookSender implements CommandHandler {

    public LinkCommandHandler(MailService mailService, UserRepository userRepository) {
        super(mailService, userRepository);
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId, Object... args) throws TelegramApiException {
        String url = update.getMessage().getText();

        if (url.contains("telegra.ph")) {
            Message processingMessage = sendProcessingMessage (absSender, chatId);
            String articleName = url.split("/")[url.split("/").length-1];
            TelegraphArticle article = getTelegraphArticle (articleName);
            System.out.println(article.description());
            File book = createBook(
                article.title(),
                article.author(),
                article.description());
            sendBook (chatId, book);
            deleteMessage (absSender, processingMessage);
            sendSentMessage (absSender, chatId);
            return;
        }

        if (url.contains("teletype.in")){
            return;
        }

        sendInvalidWebsiteMessage(absSender, chatId);
    }

    private File createBook (String title, String author, String text) {
        if (title.length() > MAX_TITLE_LENGTH)
            title = title.substring(0, MAX_TITLE_LENGTH);
        text = title + "<br><br>" + text.replaceAll("\\\\r?\\\\n", "<br>");
        System.out.println(text);
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

    private TelegraphArticle getTelegraphArticle (String name) {
        URI uri = URI.create("https://api.telegra.ph/getPage/" + name);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(uri)
            .GET()
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject result = new Gson().fromJson(response.body(), JsonObject.class)
                .getAsJsonObject("result");

            return new TelegraphArticle(
                result.get("title").getAsString(),
                result.get("author_name").getAsString(),
                result.get("description").getAsString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInvalidWebsiteMessage (AbsSender absSender, long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("❌ I can send articles only from telegra.ph or teletype.in!")
            .build();
        absSender.execute(sendMessage);
    }

    @Override
    public Command getCommand() {
        return Command.LINK;
    }
}
