package com.gorges.bot.utils;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public interface ConvertResult {

    void done (File book) throws TelegramApiException;

}
