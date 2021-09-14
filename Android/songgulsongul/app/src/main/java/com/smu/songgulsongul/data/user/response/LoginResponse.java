package com.smu.songgulsongul.data.user.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private final int code;

    @SerializedName("id")
    private final int id;

    public LoginResponse(int code, int id) {
        this.code = code;
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public int getId() {
        return id;
    }
}