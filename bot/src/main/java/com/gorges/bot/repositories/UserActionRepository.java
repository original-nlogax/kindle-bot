package com.gorges.bot.repositories;

import com.gorges.bot.models.domain.UserAction;

public interface UserActionRepository {

    UserAction findByChatId(Long chatId);

    void updateByChatId(Long chatId, UserAction userAction);

    void deleteByChatId(Long chatId);

}
