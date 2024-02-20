package com.gorges.bot.repositories.database;

import com.gorges.bot.models.entities.User;
import com.gorges.bot.repositories.UserRepository;

import static com.gorges.bot.repositories.hibernate.HibernateTransactionFactory.inTransaction;
import static com.gorges.bot.repositories.hibernate.HibernateTransactionFactory.inTransactionVoid;

public class UserRepositoryDefault implements UserRepository {
    @Override
    public User findByChatId(Long chatId) {
        String query = "from User where chatId = :chatId";

        return inTransaction(session ->
            session.createQuery(query, User.class)
                .setParameter("chatId", chatId)
                .setMaxResults(1)
                .uniqueResult()
        );
    }

    @Override
    public User findByUsername(String username) {
        String query = "from User where username = :username";

        return inTransaction(session ->
            session.createQuery(query, User.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .uniqueResult()
        );
    }

    @Override
    public void save(User user) {
        inTransactionVoid(session -> session.persist(user));
    }

    @Override
    public void update(User user) {
        inTransactionVoid(session -> session.merge(user));
    }
}
