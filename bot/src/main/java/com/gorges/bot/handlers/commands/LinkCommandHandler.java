package com.gorges.bot.handlers.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.models.domain.TelegraphArticle;
import com.gorges.bot.repositories.UserRepository;
import com.gorges.bot.services.MailService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class LinkCommandHandler extends AbstractBookSender implements CommandHandler {

    public LinkCommandHandler(MailService mailService, UserRepository userRepository) {
        super(mailService, userRepository);
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId, Object... args) throws TelegramApiException {
        String url = update.getMessage().getText();

        if (url.contains("telegra.ph")) {
            Message sendingMessage = sendSendingMessage(absSender, chatId);
            String articleName = url.split("/")[url.split("/").length-1];

            TelegraphArticle article = getTelegraphArticle (articleName);
            File book = createBook (article.title(), article.author(), article.text());
            sendBook (chatId, book);

            deleteMessage (absSender, sendingMessage);
            sendSentMessage (absSender, chatId);
            return;
        }

        if (url.contains("teletype.in")) {
            return;
        }

        sendInvalidWebsiteMessage(absSender, chatId);
    }

    private TelegraphArticle getTelegraphArticle (String name) {
        URI uri = URI.create("https://api.telegra.ph/getPage/" + name + "?return_content=true");

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
            List<JsonElement> elements = result.get("content").getAsJsonArray().asList();

            List<JsonArray> arrays = elements.stream()
                .map(JsonElement::getAsJsonObject)
                .map(o -> o.get("children").getAsJsonArray())
                .toList();

            // todo remake as stream
            StringBuilder sb = new StringBuilder();
            for (JsonArray arr : arrays) {
                for (JsonElement el : arr.asList()) {
                    sb.append(getTelegraphNodeHtml(el));
                }

                sb.append("<br>");
            }

            return new TelegraphArticle(
                result.get("title").getAsString(),
                result.get("author_name").getAsString(),
                sb.toString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTelegraphNodeHtml(JsonElement el) {
        if (el.isJsonObject()) {
            JsonObject object = el.getAsJsonObject();

            String tag = object.get("tag").getAsString();
            boolean closeTag =
                !tag.equals("br") &&
                !tag.equals("hr") &&
                !tag.equals("img");
            if  (tag.equals("a")) {
                // todo process links
            }

            if (object.has("children")) {
                final String openingTag = "<" + tag + ">";
                final String closingTag = closeTag ? ("</" + tag + ">") : "";
                return openingTag + object.getAsJsonArray("children").asList()
                    .stream().map(this::getTelegraphNodeHtml)
                    .collect(Collectors.joining()) + closingTag;
            }
        } else {
            return el.getAsString();
        }

        return "";
    }

    private void sendInvalidWebsiteMessage (AbsSender absSender, long chatId) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("❌ Я могу отправлять статьи только с telegra.ph или teletype.in!")
            .build();
        absSender.execute(sendMessage);
    }

    @Override
    public Command getCommand() {
        return Command.LINK;
    }
}
