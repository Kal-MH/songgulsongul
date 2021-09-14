package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class JoinData {
    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    @SerializedName("login_id")
    private final String login_id;

    @SerializedName("sns_url")
    private final String sns_url;

    public JoinData(String email, String password, String login_id, String sns_url) {
        this.email = email;
        this.password = password;
        this.login_id = login_id;
        this.sns_url = sns_url;
    }
}