package com.smu.songgulsongul.data.user;

import com.google.gson.annotations.SerializedName;

public class TokenData {

    @SerializedName("userid")
    int userId;

    @SerializedName("token")
    String token;

    public TokenData(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
