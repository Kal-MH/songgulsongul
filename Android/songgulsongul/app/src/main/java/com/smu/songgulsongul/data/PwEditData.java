package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class PwEditData {
    @SerializedName("userid")
    int user_id;

    @SerializedName("password")
    String password;

    public PwEditData(int user_id, String password){
        this.user_id = user_id;
        this.password = password;
    }
}
