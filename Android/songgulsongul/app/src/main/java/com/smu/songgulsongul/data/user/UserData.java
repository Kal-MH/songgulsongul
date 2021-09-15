package com.smu.songgulsongul.data.user;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("id")
    String id;

    @SerializedName("user_id")
    String userId;

    @SerializedName("status")
    int status;

    public UserData(String id, int status) {
        this.id = id;
        this.status = status;
    }

    public void SetUserId(String user_id) {
        this.userId = user_id;
    }
}
