package com.gorges.bot.services.impl;

import com.gorges.bot.core.Config;
import com.gorges.bot.services.MailService;
import com.gorges.bot.utils.Utils;
import jakarta.activation.FileDataSource;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.io.File;
import java.io.IOException;

public class MailServiceDefault implements MailService {

    private final String from;
    private final String password;
    private final String smtpHost;
    private final int smtpPort;
    private final Mailer mailer;

    public MailServiceDefault (Config config) {
        smtpHost = config.get("mail.smtp.host");
        smtpPort = Integer.parseInt(config.get("mail.smtp.port"));
        from = config.get("mail.from");
        password = config.get("mail.password");

        mailer = MailerBuilder
            .withSMTPServer(smtpHost, smtpPort, from, password)
            .buildMailer();
    }

    @Override
    public void send(final File book, String to) {
        System.out.println("Sending book to " + to + "...");
        /*
        try {
            book.renameTo(File.createTempFile("book", ".epub", book.getParentFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        Email email = EmailBuilder.startingBlank()
            .from("Bot", from)
            .to(null, to)
            .withAttachment(book.getName(), new FileDataSource(book))
            .buildEmail();

        mailer.sendMail(email);

        //book.delete();
    }
}
