package com.smu.songgulsongul.data.user;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("login_id")
    String loginId;

    @SerializedName("password")
    String password;

    public LoginData(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}