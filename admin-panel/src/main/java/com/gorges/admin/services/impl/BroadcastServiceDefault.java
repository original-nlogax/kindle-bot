package com.gorges.admin.services.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gorges.admin.models.entities.User;
import com.gorges.admin.services.BroadcastService;
import com.gorges.admin.services.UserService;
import com.gorges.admin.services.TelegramService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BroadcastServiceDefault implements BroadcastService {

    private static final Logger LOG = LogManager.getLogger(BroadcastServiceDefault.class);

    private final TelegramService telegramService;
    private final UserService userService;

    private final ExecutorService broadcastQueue = Executors.newSingleThreadExecutor();

    @Autowired
    public BroadcastServiceDefault(TelegramService telegramService, UserService userService) {
        this.telegramService = telegramService;
        this.userService = userService;
    }

    @Override
    public void send(String message) {
        broadcastQueue.submit(() -> {
            sendMessageToClients(message, userService.findAllByActive(true));
        });
    }

    private void sendMessageToClients(String message, List<User> users) {
        for (User user : users) {
            try {
                telegramService.sendMessage(user.getChatId(), message);
            } catch (Exception e) {
                if (e.toString().contains("Forbidden: bot was blocked by the user")) {
                    deactivateClient(user);
                } else {
                    LOG.error("Failed send message '{}' to client '{}'", message, user, e);
                }
            }
        }
    }

    private void deactivateClient(User user) {
        user.setActive(false);
        userService.update(user);
    }

}
