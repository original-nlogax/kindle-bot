package com.gorges.admin.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Entity
@Table(name = "admins")
public class Admin {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Напишите никнейм")
    @Length(max = 255, message = "Никнейм слишком длинный (больше 255 символов)")
    private String username;

    // for some reason without the @Column Hibernate generates chatId sql field without an
    // underscore (no camelcase)
    @Column(name="chat_id")
    @Id
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

    @Override
    public String toString() {
        return "Admin{" +
            "username='" + username + '\'' +
            ", chatId=" + chatId +
            '}';
    }
}
