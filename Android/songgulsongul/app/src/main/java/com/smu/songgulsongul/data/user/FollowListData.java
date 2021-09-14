package com.smu.songgulsongul.data.user;

import com.google.gson.annotations.SerializedName;

public class FollowListData {
    @SerializedName("id")
    private final String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("status")
    private int status;

    public FollowListData(String id) {
        this.id = id;
    }

    public void addUserId(String user_id) {
        this.userId = user_id;
    }

    public void addStatus(int status) {
        this.status = status;
    }
}
