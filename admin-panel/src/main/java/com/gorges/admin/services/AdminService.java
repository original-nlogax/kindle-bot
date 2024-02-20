package com.gorges.admin.services;

import java.util.List;

import com.gorges.admin.models.entities.Admin;

public interface AdminService {
    List<Admin> findAll();

    Admin save(Admin admin);

    Admin findByUsername(String username);

    Admin findByChatId(Long chatId);

    void deleteByChatId(Long chatId);
}
