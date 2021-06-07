package com.smu.songgulsongul.responseData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KeepResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("keepInfo")
    private List<Post> keepInfo;

    @SerializedName("keepCnt")
    private int keepCnt;

    @SerializedName("profileImg")
    private String profileImg;


    public int getCode() {
        return code;
    }

    public List<Post> getKeepinfo() {
        return keepInfo;
    }

    public int getKeepcnt() {
        return keepCnt;
    }

    public String getProfileImg() {
        return profileImg;
    }
}
