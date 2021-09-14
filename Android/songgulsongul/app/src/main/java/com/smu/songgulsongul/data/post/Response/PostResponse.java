package com.smu.songgulsongul.data.post.Response;

import com.google.gson.annotations.SerializedName;
import com.smu.songgulsongul.data.post.PostDetail;


public class PostResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private PostDetail data;

    public int getCode() {
        return code;
    }

    public PostDetail getData() {
        return data;
    }
}
