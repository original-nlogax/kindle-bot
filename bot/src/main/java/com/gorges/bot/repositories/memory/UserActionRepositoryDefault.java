package com.gorges.bot.repositories.memory;

import com.gorges.bot.models.domain.UserAction;
import com.gorges.bot.repositories.UserActionRepository;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserActionRepositoryDefault implements UserActionRepository {

    private final Map<Long, UserAction> usersActions = new ConcurrentHashMap<>();

    @Override
    public UserAction findByChatId(Long chatId) {
        UserAction userAction = usersActions.get(chatId);
        return SerializationUtils.clone(userAction);
    }

    @Override
    public void updateByChatId(Long chatId, UserAction userAction) {
        usersActions.put(chatId, SerializationUtils.clone(userAction));
    }

    @Override
    public void deleteByChatId(Long chatId) {
        usersActions.remove(chatId);
    }

}
