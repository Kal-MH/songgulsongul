package com.smu.songgulsongul.data.post;

import com.google.gson.annotations.SerializedName;
import com.smu.songgulsongul.recycler_item.ItemTag;

import java.util.List;

public class PostEditData {
    @SerializedName("user_id")
    int userId;

    @SerializedName("post_id")
    int postId;

    @SerializedName("text")
    String text;// 글 내용


    @SerializedName("hash_tag")
    List<String> hashTag;// 해쉬태그


    @SerializedName("item_tag")
    List<ItemTag> itemTags;// 아이템태그


    @SerializedName("ccl")
    int[] ccl; // ccl 설정값


    public PostEditData(int userId, int postId, String text, List<String> hashTag, int[] ccl, List<ItemTag> itemTags) {
        this.userId = userId;
        this.postId = postId;
        this.text = text;
        this.hashTag = hashTag;
        this.ccl = ccl;
        this.itemTags = itemTags;
    }
}
