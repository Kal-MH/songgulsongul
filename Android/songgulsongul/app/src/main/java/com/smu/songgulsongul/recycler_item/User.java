package com.smu.songgulsongul.recycler_item;

public class User {
    public User(int user_id, String login_id, String img_profile) {
        this.user_id = user_id;
        this.login_id = login_id;
        this.img_profile = img_profile;
    }

    public User(String userId, String img_profile, String intro, String sns, int point, int sns_check) {
        this.userId = userId;
        this.img_profile = img_profile;
        this.intro = intro;
        this.sns = sns;
        this.point = point;
        this.sns_check = sns_check;
    }

    int user_id;
    String login_id;
    String userId;
    String img_profile;
    String intro;
    String sns;
    int point;
    int sns_check;

    public int getSnsCheck() {
        return sns_check;
    }

    public void setSnsCheck(int snsCheck) {
        this.sns_check = sns_check;
    }

    int flag;

    public int getFlag() {
        return flag;
    }

    public String getSns() {
        return sns;
    }

    public int getPoint() {
        return point;
    }

    public String getIntro() {
        return intro;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public String getImg_profile() {
        return img_profile;
    }

    public String getUserId() {
        return userId;
    }
}