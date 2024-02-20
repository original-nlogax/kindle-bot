package com.gorges.bot.handlers.commands;

import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.Command;
import com.gorges.bot.models.domain.MultiMessage;
import com.gorges.bot.repositories.MultiMessageRepository;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Collectors;

public class ProcessRepliesCommandHandler implements CommandHandler {

    public static final String REPLIES_DELIMITER = "\n\n";

    private final MultiMessageRepository multiMessageRepository;

    public ProcessRepliesCommandHandler(MultiMessageRepository multiMessageRepository) {
        this.multiMessageRepository = multiMessageRepository;

    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        MultiMessage multiMessage = multiMessageRepository.getByChatId(chatId);
        String text = multiMessage.getMessages().stream()
            .map(Message::getText)
            .collect(Collectors.joining(REPLIES_DELIMITER));

        System.out.println(text);
    }

    @Override
    public Command getCommand() {
        return Command.PROCESS_REPLIES;
    }
}
