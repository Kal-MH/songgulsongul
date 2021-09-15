package com.smu.songgulsongul.data.comment;

import com.google.gson.annotations.SerializedName;

public class CommentData {

    @SerializedName("userid")
    int userId;
    @SerializedName("postid")
    int postId;
    @SerializedName("comment")
    String comment;

    public CommentData(int userId, int postId, String comment) {
        this.userId = userId;
        this.postId = postId;
        this.comment = comment;
    }
}