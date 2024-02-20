package com.gorges.bot.repositories;

import com.gorges.bot.models.entities.User;

public interface UserRepository {
    User findByChatId(Long chatId);
    User findByUsername(String username);

    void save(User user);
    void update(User user);
}
