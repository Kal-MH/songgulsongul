package com.smu.songgulsongul.data.user;

import com.google.gson.annotations.SerializedName;

public class PwEditData {
    @SerializedName("userid")
    int userId;

    @SerializedName("password")
    String password;

    public PwEditData(int userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
