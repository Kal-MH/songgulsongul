package com.smu.songgulsongul.data.user;

import com.google.gson.annotations.SerializedName;

public class FindData {
    @SerializedName("email")
    String email;

    @SerializedName("login_id")
    String loginId;

    public FindData(String email, String loginId) {
        this.email = email;
        this.loginId = loginId;
    }
}
