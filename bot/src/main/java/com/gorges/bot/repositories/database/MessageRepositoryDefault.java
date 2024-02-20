package com.gorges.bot.repositories.database;

import static com.gorges.bot.repositories.hibernate.HibernateTransactionFactory.inTransaction;

import com.gorges.bot.models.entities.Message;
import com.gorges.bot.repositories.MessageRepository;

public class MessageRepositoryDefault implements MessageRepository {

    @Override
    public Message findByName(String messageName) {
        String query = "from Message where name = :name";

        return inTransaction(session ->
                session.createQuery(query, Message.class)
                        .setParameter("name", messageName)
                        .setMaxResults(1)
                        .uniqueResult()
        );
    }

}
