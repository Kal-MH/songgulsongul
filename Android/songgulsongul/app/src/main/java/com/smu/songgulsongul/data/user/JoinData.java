package com.smu.songgulsongul.data.user;

import com.google.gson.annotations.SerializedName;

public class JoinData {
    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    @SerializedName("login_id")
    private final String loginId;

    @SerializedName("sns_url")
    private final String snsUrl;

    public JoinData(String email, String password, String loginId, String snsUrl) {
        this.email = email;
        this.password = password;
        this.loginId = loginId;
        this.snsUrl = snsUrl;
    }
}