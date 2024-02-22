package com.gorges.bot.services;

import java.io.File;

public interface BookConverterService {

    // todo maybe get resources path by code?
    String CMD_LINUX_DIR = "bot\\src\\main\\resources\\cmd\\linux\\";
    String CMD_WINDOWS_DIR = "bot\\src\\main\\resources\\cmd\\windows\\";

    File convert (File book);

    File fb2ToEpub (File book);

    File optimizePdf (File book);

}
