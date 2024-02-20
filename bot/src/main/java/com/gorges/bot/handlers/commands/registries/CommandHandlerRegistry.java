package com.gorges.bot.handlers.commands.registries;

import java.util.List;

import com.gorges.bot.handlers.CommandHandler;
import com.gorges.bot.models.domain.Command;

public interface CommandHandlerRegistry {

    void setCommandHandlers(List<CommandHandler> commandHandlers);

    CommandHandler find(Command command);

}
