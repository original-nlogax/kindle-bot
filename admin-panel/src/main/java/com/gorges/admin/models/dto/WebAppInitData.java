package com.gorges.admin.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebAppInitData {
    public String initData;
    public InitDataUnsafe initDataUnsafe;
    public String version;
    public String platform;
    public String colorScheme;
    public ThemeParams themeParams;
    public boolean isExpanded;
    public int viewportHeight;
    public int viewportStableHeight;
    public boolean isClosingConfirmationEnabled;
    public String headerColor;
    public String backgroundColor;
    @JsonProperty("BackButton")
    public BackButton backButton;
    @JsonProperty("MainButton")
    public MainButton mainButton;
    @JsonProperty("SettingsButton")
    public SettingsButton settingsButton;
    @JsonProperty("HapticFeedback")
    public HapticFeedback hapticFeedback;
    @JsonProperty("CloudStorage")
    public CloudStorage cloudStorage;

    public WebAppInitData () {

    }

    public WebAppInitData(String initData, String version, String platform, String colorScheme, boolean isExpanded, int viewportHeight, int viewportStableHeight, boolean isClosingConfirmationEnabled, String headerColor, String backgroundColor) {
        this.initData = initData;
        this.version = version;
        this.platform = platform;
        this.colorScheme = colorScheme;
        this.isExpanded = isExpanded;
        this.viewportHeight = viewportHeight;
        this.viewportStableHeight = viewportStableHeight;
        this.isClosingConfirmationEnabled = isClosingConfirmationEnabled;
        this.headerColor = headerColor;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return "WebAppInitData{" +
            "initData='" + initData + '\'' +
            ", initDataUnsafe=" + initDataUnsafe +
            ", version='" + version + '\'' +
            ", platform='" + platform + '\'' +
            ", colorScheme='" + colorScheme + '\'' +
            ", themeParams=" + themeParams +
            ", isExpanded=" + isExpanded +
            ", viewportHeight=" + viewportHeight +
            ", viewportStableHeight=" + viewportStableHeight +
            ", isClosingConfirmationEnabled=" + isClosingConfirmationEnabled +
            ", headerColor='" + headerColor + '\'' +
            ", backgroundColor='" + backgroundColor + '\'' +
            ", backButton=" + backButton +
            ", mainButton=" + mainButton +
            ", settingsButton=" + settingsButton +
            ", hapticFeedback=" + hapticFeedback +
            ", cloudStorage=" + cloudStorage +
            '}';
    }
}
