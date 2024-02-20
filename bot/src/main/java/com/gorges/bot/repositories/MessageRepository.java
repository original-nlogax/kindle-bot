package com.gorges.bot.repositories;

import com.gorges.bot.models.entities.Message;

public interface MessageRepository {

    Message findByName(String messageName);

}
