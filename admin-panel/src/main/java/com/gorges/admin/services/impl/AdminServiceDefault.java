package com.gorges.admin.services.impl;

import java.util.List;

import com.gorges.admin.models.entities.Admin;
import com.gorges.admin.services.AdminService;
import org.springframework.stereotype.Service;

import com.gorges.admin.exceptions.ValidationException;
import com.gorges.admin.repositories.AdminRepository;

@Service
public class AdminServiceDefault implements AdminService {

    private final AdminRepository repository;

    public AdminServiceDefault(AdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Admin> findAll() {
        return repository.findAll();
    }

    @Override
    public Admin save(Admin admin) {
        if (admin == null) {
            throw new IllegalArgumentException("Admin should not be NULL");
        }
        if (admin.getChatId() == null) {
            throw new ValidationException("ChatId of Admin should not be NULL");
        }

        return repository.save(admin);
    }

    /*
    @Override
    public Admin update(Admin admin) {
        if (admin == null) {
            throw new IllegalArgumentException("User should not be NULL");
        }
        if (admin.getId() == null) {
            throw new ValidationException("Id of User should not be NULL");
        }

        Admin receivedAdmin = findById(admin.getId());
        receivedAdmin.setUsername(admin.getUsername());
        return repository.save(receivedAdmin);
    }*/

    @Override
    public Admin findByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username should not be NULL");
        }

        if (username.isBlank()) {
            throw new IllegalArgumentException("Username should not be blank");
        }

        return repository.findByUsername(username).orElse(null);
    }

    @Override
    public Admin findByChatId(Long chatId) {
        return repository.findByChatId(chatId).orElse(null);
    }

    @Override
    public void deleteByChatId(Long chatId) {
        if (chatId == null) {
            throw new IllegalArgumentException("ChatId of Admin should not be NULL");
        }

        repository.deleteById(chatId);
    }
}
