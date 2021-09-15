package com.smu.songgulsongul.data.user.response;

import com.google.gson.annotations.SerializedName;
import com.smu.songgulsongul.recycler_item.User;

import java.util.List;

public class SearchIdResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private List<User> data;

    public int getCode() {
        return code;
    }

    public List<User> getData() {
        return data;
    }
}
