package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class FollowData {
    @SerializedName("loginId")
    private final String loginId;

    @SerializedName("userId")
    private final String userId;

    public FollowData(String loginId, String userId) {
        this.loginId = loginId;
        this.userId = userId;
    }
}
