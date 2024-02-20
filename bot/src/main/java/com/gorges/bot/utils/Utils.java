package com.gorges.bot.utils;

import com.gorges.bot.models.domain.Button;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isText (String text) {
        return matches (text, "[a-zA-Z]|[U+0400–U+04FF]");
    }

    public static boolean isNumber (String text) {
        return matches (text, "[0-9]+$");
    }

    public static boolean matches (String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(text).find();
    }
}
