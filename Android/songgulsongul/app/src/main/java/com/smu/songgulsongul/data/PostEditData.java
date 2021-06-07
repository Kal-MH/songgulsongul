package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.smu.songgulsongul.responseData.ItemTag;

public class PostEditData {
    @SerializedName("user_id")
    int user_id;

    @SerializedName("post_id")
    int post_id;

    @SerializedName("text") // 글 내용
    String text;

    @SerializedName("hash_tag") // 해쉬태그
    List<String> hash_tag;

    @SerializedName("item_tag") // 아이템태그
    List<ItemTag> item_tag;

    @SerializedName("ccl") // ccl설정값
    int ccl[];

    public PostEditData(){

    }

    public PostEditData(int user_id, int post_id, String text, List<String> hash_tag, int ccl[], List<ItemTag> item_tag){
        this.user_id = user_id;
        this.post_id = post_id;
        this.text = text;
        this.hash_tag = hash_tag;
        this.ccl = ccl;
        this.item_tag = item_tag;
    }
}
