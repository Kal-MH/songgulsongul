package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class IdCheckData {
    @SerializedName("login_id")
    private String login_id;

    public IdCheckData(String login_id){
        this.login_id = login_id;
    }
}
