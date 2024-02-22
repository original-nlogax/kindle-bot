package com.gorges.bot.services.impl;

import com.gorges.bot.services.BookConverterService;
import com.gorges.bot.utils.OSValidator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BookConverterServiceDefault implements BookConverterService {

    @Override
    public File convert(File book) {
        String format = book.getName().split("\\.")[1];
        return switch (format) {
            case "fb2" -> fb2ToEpub(book);
            case "pdf" -> optimizePdf(book);
            default -> book;
        };
    }

    @Override
    public File fb2ToEpub(File book) {
        Path sourcePath = Paths.get(book.getAbsolutePath());
        String sourceFolder = sourcePath.getParent().toString();
        Path destPath = Paths.get(sourceFolder + "/");

        String exec = getCmdPath()
            + "fb2c" + (OSValidator.isWindows() ? ".exe" : "")
            + " convert --to epub "
            + "\"" + sourcePath + "\" "
            + "\"" + destPath + "\"";

        run(exec);

        return destPath.toFile();
    }

    @Override
    public File optimizePdf(File book) {
        Path sourcePath = Paths.get(book.getAbsolutePath());
        String sourceFolder = sourcePath.getParent().toString();
        Path destPath = Paths.get(sourceFolder + "/");

        String exec = getCmdPath()
            + "k2pdfopt" + (OSValidator.isWindows() ? ".exe" : "")
            + " \"" + sourcePath + "\""
            + " -fc- -odpi 220 -o \"" + destPath + "\"";

        run(exec);

        return destPath.toFile();
    }

    private void run (String exec) {
        System.out.println(exec);
        final ProcessBuilder pb = new ProcessBuilder(exec.split(" "));

        pb.inheritIO();

        try {
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCmdPath () {
        if (OSValidator.isWindows()) return CMD_WINDOWS_DIR;
        if (OSValidator.isUnix()) return CMD_LINUX_DIR;
        /*
        String cmdUrl = "";
        if (OSValidator.isWindows()) cmdUrl = CMD_WINDOWS_DIR;
        if (OSValidator.isUnix()) cmdUrl = CMD_LINUX_DIR;
        /*
        try {
            return Path.of(ClassLoader.getSystemResource(cmdUrl).toURI()).toAbsolutePath().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ;*/
        return "";
    }

}
