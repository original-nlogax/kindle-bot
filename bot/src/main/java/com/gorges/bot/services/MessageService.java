package com.gorges.bot.services;

import com.gorges.bot.models.entities.Message;

public interface MessageService {

    Message findByName(String messageName);

}
