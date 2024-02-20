package com.gorges.bot.repositories;

import com.gorges.bot.models.domain.Command;

public interface UserCommandStateRepository {

    void pushByChatId(Long chatId, Command command);

    Command popByChatId(Long chatId);

    void deleteAllByChatId(Long chatId);

}
