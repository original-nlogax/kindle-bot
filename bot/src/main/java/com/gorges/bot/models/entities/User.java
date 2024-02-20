package com.gorges.bot.models.entities;

import jakarta.persistence.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="users")
public class User {
    @Id
    // for some reason without the @Column Hibernate generates chatId sql field without an
    // underscore (no camelcase)
    @Column(name="chat_id")
    private Long chatId;

    @NotNull
    private String username;

    private boolean active = true;

    public User (Long chatId) {
        this.chatId = chatId;
    }

    public User() {
        this(0L);
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
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
        User user = (User) o;
        return Objects.equals(chatId, user.chatId) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, username);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
