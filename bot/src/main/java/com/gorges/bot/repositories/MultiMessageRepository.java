package com.gorges.bot.repositories;

import com.gorges.bot.models.domain.MultiMessage;

public interface MultiMessageRepository {

    MultiMessage getByChatId (long chatId);

    void save (long chatId, MultiMessage multiMessage);

    void delete(Long chatId);

}
