package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class FindData {
    @SerializedName("email")
    String email;

    @SerializedName("login_id")
    String login_id;

    public FindData(String email, String login_id){
        this.email = email;
        this.login_id = login_id;
    }
}
