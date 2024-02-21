package com.gorges.bot.models.domain;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MultiMessage {

    public final static float WAIT_SECONDS = 0.5f;

    private Timer timer;
    private List<Message> messages;
    private Runnable runnable;

    public MultiMessage() {
        messages = new ArrayList<>();
        timer = new Timer();
    }

    public void startCollecting () {
        if (runnable == null) {
            System.out.println("MultiMessage's runnable can't be null");
            return;
        }

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };

        timer.cancel();
        timer = new Timer();
        timer.schedule(task, (long)(WAIT_SECONDS*1000));
    }

    public void setCallback (Runnable runnable) {
        this.runnable = runnable;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage (Message message) {
        // todo if (!timer.isRunning())  return (or !isCollecting)
        // (although it shouldn't be in repository after timer completion)
        this.messages.add(message);
    }
}
