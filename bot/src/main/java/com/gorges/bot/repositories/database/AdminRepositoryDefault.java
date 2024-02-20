package com.gorges.bot.repositories.database;

import com.gorges.bot.models.entities.Admin;
import com.gorges.bot.repositories.AdminRepository;

import static com.gorges.bot.repositories.hibernate.HibernateTransactionFactory.inTransaction;

public class AdminRepositoryDefault implements AdminRepository {

    @Override
    public Admin findByChatId(Long chatId) {
        String query = "from Admin where chatId = :chatId";

        return inTransaction(session ->
            session.createQuery(query, Admin.class)
                .setParameter("chatId", chatId)
                .setMaxResults(1)
                .uniqueResult()
        );
    }

    @Override
    public Admin findByUsername(String username) {
        String query = "from Admin where username = :username";

        return inTransaction(session ->
            session.createQuery(query, Admin.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .uniqueResult()
        );
    }

    @Override
    public boolean isAdmin(Long chatId) {
        return findByChatId(chatId) != null;
    }

    @Override
    public boolean isAdmin(String username) {
        return findByUsername(username) != null;
    }
}
