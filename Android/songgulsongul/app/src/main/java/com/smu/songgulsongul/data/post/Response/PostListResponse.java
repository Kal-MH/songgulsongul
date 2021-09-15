package com.smu.songgulsongul.data.post.Response;

import com.google.gson.annotations.SerializedName;
import com.smu.songgulsongul.recycler_item.Post;

import java.util.List;

public class PostListResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private List<Post> data;

    public int getCode() {
        return code;
    }

    public List<Post> getData() {
        return data;
    }

    public void addData(List<Post> p) {
        for (Post post : p)
            data.add(post);
    }
}
