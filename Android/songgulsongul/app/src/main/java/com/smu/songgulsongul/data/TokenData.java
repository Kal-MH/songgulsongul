package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;


public class TokenData {

    @SerializedName("userid")
    int userid;

    @SerializedName("token")
    String token;

    public TokenData(int userid, String token) {
        this.userid = userid;
        this.token = token;
    }
}
