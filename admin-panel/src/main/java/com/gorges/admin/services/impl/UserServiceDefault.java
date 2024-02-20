package com.gorges.admin.services.impl;

import java.util.List;

import com.gorges.admin.models.entities.User;
import com.gorges.admin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gorges.admin.exceptions.ValidationException;
import com.gorges.admin.repositories.UserRepository;

@Service
public class UserServiceDefault implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceDefault(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User findByChatId(Long chatId) {
        if (chatId == null) {
            throw new IllegalArgumentException("ChatId of User should not be NULL");
        }

        return repository.findById(chatId).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Client should not be NULL");
        }
        if (user.getChatId() == null) {
            throw new ValidationException("ChatId of User should not be NULL");
        }

        return repository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username should not be NULL");
        }

        if (username.isBlank()) {
            throw new IllegalArgumentException("Username should not be blank");
        }

        return repository.findByUsername(username);
    }

    @Override
    public List<User> findAllByActive(boolean active) {
        return repository.findAllByActive(active);
    }

}
