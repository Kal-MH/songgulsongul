package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class LikeData {
    @SerializedName("loginId")
    String loginId;

    @SerializedName("postId")
    int postId;

    @SerializedName("likeTime")
    Date date;

    public LikeData(String loginId, int postId, Date date){
        this.loginId = loginId;
        this.postId = postId;
        this.date = date;
    }
}
