package com.smu.songgulsongul.data.user;

import com.google.gson.annotations.SerializedName;

public class EmailData {
    @SerializedName("email")
    String email;

    public EmailData(String email) {
        this.email = email;
    }
}
