package com.gorges.bot.repositories;

import com.gorges.bot.models.entities.Admin;

public interface AdminRepository {
    Admin findByChatId(Long chatId);
    Admin findByUsername(String username);

    boolean isAdmin(Long chatId);

    boolean isAdmin(String username);
}
