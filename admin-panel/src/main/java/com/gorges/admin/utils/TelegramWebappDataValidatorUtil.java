package com.gorges.admin.utils;

import com.gorges.admin.models.dto.InitDataUnsafe;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Validates Telegram Bot WebApp InitData via `HMAC_SHA_256`
 */
public class TelegramWebappDataValidatorUtil {

    public static boolean isValid(String initData, String telegramBotToken) {
        Map<String, String> initDataMap = parseInitData(initData);
        String hash = initDataMap.get("hash");
        initDataMap.remove("hash");

        String dataCheckString = new TreeMap<>(initDataMap)
            .entrySet()
            .stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .map(s -> URLDecoder.decode(s, StandardCharsets.UTF_8))
            .collect(Collectors.joining("\n"));

        byte[] secret = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, "WebAppData")
            .hmac(telegramBotToken);
        String dataCheckHash = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret)
            .hmacHex(dataCheckString);
        return hash.equals(dataCheckHash);
    }

    private static Map<String, String> parseInitData(String initData) {
        Map<String, String> initDataMap = new HashMap<>();
        String[] keyValuePairs = initData.split("&");

        for (String keyValuePair : keyValuePairs) {
            String[] parts = keyValuePair.split("=", 2);
            if (parts.length == 2) {
                initDataMap.put(parts[0], parts[1]);
            }
        }
        return initDataMap;
    }
}
