package com.gorges.bot.models.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "admins")
public class Admin {

    @Column(unique = true, nullable = false)
    private String username;

    @Id
    @Column(name="chat_id")
    private Long chatId;

    public Admin() {
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(username, admin.username) && Objects.equals(chatId, admin.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, chatId);
    }
}
