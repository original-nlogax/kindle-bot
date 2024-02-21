package com.gorges.bot.repositories.memory;

import com.gorges.bot.models.domain.MultiMessage;
import com.gorges.bot.repositories.MultiMessageRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiMessageRepositoryDefault implements MultiMessageRepository {

    private final Map<Long, MultiMessage> usersMultiMessages = new ConcurrentHashMap<>();

    @Override
    public MultiMessage getByChatId (long chatId) {
        return usersMultiMessages.get(chatId);
    }

    @Override
    public void save(long chatId, MultiMessage multiMessage) {
        usersMultiMessages.put(chatId, multiMessage);
    }

    @Override
    public void delete(Long chatId) {
        usersMultiMessages.remove(chatId);
    }
}
