package com.smu.songgulsongul.data.notification;

import com.google.gson.annotations.SerializedName;

public class RequestNotification {

    @SerializedName("sender") // 보낸사람 id
    private int sender;

    @SerializedName("loginid") // 알림받을 사람의 loginid
    private String loginId;

    @SerializedName("postid") //알림 받을 사람의 postid
    private int postId;

    @SerializedName("notification")
    private NotificationData notification;

    private int mode;

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setSendNotificationModel(NotificationData notification) {
        this.notification = notification;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
}