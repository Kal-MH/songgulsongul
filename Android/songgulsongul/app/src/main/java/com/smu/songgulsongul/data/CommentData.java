package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class CommentData  {

    @SerializedName("userid")
    int userid;
    @SerializedName("postid")
    int postid;
    @SerializedName("comment")
    String comment;

    public CommentData(int userid, int postid, String comment) {
        this.userid = userid;
        this.postid = postid;
        this.comment = comment;
    }
}