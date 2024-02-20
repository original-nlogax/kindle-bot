package com.gorges.admin.models.dto;

public class User {
    private long id;
    private String first_name;
    private String last_name;
    private String username;
    private String language_code;
    private boolean allows_write_to_pm;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public boolean isAllows_write_to_pm() {
        return allows_write_to_pm;
    }

    public void setAllows_write_to_pm(boolean allows_write_to_pm) {
        this.allows_write_to_pm = allows_write_to_pm;
    }
}

