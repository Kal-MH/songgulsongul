package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class RequestNotification {

    @SerializedName("sender") // 보낸사람 id
    private int sender;


    @SerializedName("loginid") // 알림받을 사람의 loginid
    private String loginid;

    @SerializedName("postid") //알림 받을 사람의 postid
    private  int postid;

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    @SerializedName("notification")
    private NotificationData notification;


    private  int mode;




    public NotificationData getNotification() {
        return notification;
    }

    public void setSendNotificationModel(NotificationData notification) {
        this.notification = notification;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
}