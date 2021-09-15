package com.smu.songgulsongul.data.user;

import com.google.gson.annotations.SerializedName;

public class IdCheckData {
    @SerializedName("login_id")
    private final String loginId;

    public IdCheckData(String loginId) {
        this.loginId = loginId;
    }
}
