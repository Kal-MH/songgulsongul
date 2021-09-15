package com.smu.songgulsongul.data.post;

import com.google.gson.annotations.SerializedName;

public class KeepData {
    @SerializedName("login_id")
    String loginId;

    public KeepData(String loginId) {
        this.loginId = loginId;
    }

}
