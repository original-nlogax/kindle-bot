package com.gorges.bot.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.gorges.bot.models.entities.Message;
import com.gorges.bot.services.MessageService;
import org.apache.commons.lang3.SerializationUtils;

import com.gorges.bot.repositories.MessageRepository;
import com.gorges.bot.repositories.database.MessageRepositoryDefault;

public class MessageServiceDefault implements MessageService {

    private MessageRepository messageRepository = new MessageRepositoryDefault();

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, Message> cachedMessages = new HashMap<>();

    public MessageServiceDefault() {
        startCacheClearTask();
    }

    private void startCacheClearTask() {
        //executorService.scheduleAtFixedRate(cachedMessages::clear, 20, 20, TimeUnit.MINUTES);
    }

    public void setRepository(MessageRepository repository) {
        this.messageRepository = repository;
    }

    @Override
    public Message findByName(String messageName) {
        if (messageName == null) {
            throw new IllegalArgumentException("MessageName should not be NULL");
        }

        Message message = cachedMessages.get(messageName);
        if (message == null) {
            message = messageRepository.findByName(messageName);
            cachedMessages.put(messageName, message);
        }

        return SerializationUtils.clone(message);
    }

}
