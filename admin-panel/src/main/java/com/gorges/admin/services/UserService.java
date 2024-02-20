package com.gorges.admin.services;

import java.util.List;

import com.gorges.admin.models.entities.User;

public interface UserService {

    User findByChatId(Long chatId);

    List<User> findAll();

    User update(User user);

    User findByUsername(String username);

    List<User> findAllByActive(boolean active);
}
