package com.gorges.bot.services;

import com.gorges.bot.utils.ConvertResult;

import java.io.File;

public interface BookConverterService {

    // todo maybe get resources path by code?
    String CMD_LINUX_DIR = "bot\\src\\main\\resources\\cmd\\linux\\";
    String CMD_WINDOWS_DIR = "bot\\src\\main\\resources\\cmd\\windows\\";

    void convert (File book, ConvertResult convertResult);

    File fb2ToEpub (File book);

    File optimizePdf (File book);   // todo FIX File already exists!

}
