package com.gorges.admin.models.dto;

public class InitDataUnsafe {
    public String query_id;
    public User user;
    public String auth_date;
    public String hash;

    @Override
    public String toString() {
        return "InitDataUnsafe{" +
            "query_id='" + query_id + '\'' +
            ", user=" + user +
            ", auth_date='" + auth_date + '\'' +
            ", hash='" + hash + '\'' +
            '}';
    }
}
