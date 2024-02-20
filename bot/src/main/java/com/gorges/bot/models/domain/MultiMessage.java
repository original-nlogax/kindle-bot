package com.gorges.bot.models.domain;

public class GroupMessage {

    private Boolean isCollecting;

    public GroupMessage() {
        isCollecting = false;
    }

    public Boolean getCollecting() {
        return isCollecting;
    }

    public void setCollecting(Boolean collecting) {
        isCollecting = collecting;
    }
}
