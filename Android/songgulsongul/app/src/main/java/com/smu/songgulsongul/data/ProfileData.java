package com.smu.songgulsongul.data;

import android.graphics.Bitmap;

public class ProfileData {
    private String userId;
    Bitmap profile_image;
    private int feed_counter;
    private int follow_counter;
    private final int follower_counter;
    private String intro;
    private String url;
    private int point;

    public String getId(){
        return userId;
    }
    public Bitmap getProfileImage(){return profile_image;}
    public int getFeedCounter(){return feed_counter;}
    public int getFollowCounter(){return follow_counter;}
    public String getIntro(){return intro;}
    public String getUrl(){return url;}
    public int getPoint(){return point;}

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setProfileImage(Bitmap profile_image){this.profile_image=profile_image;}
    public void setFeedCounter(int feed_counter) {
        this.feed_counter = feed_counter;
    }
    public void setFollowCounter(int follow_counter) {
        this.follow_counter = follow_counter;
    }
    public void setFollowerCounter(int follower_counter) { this.follow_counter = follower_counter; }
    public void setIntro(String intro) {
        this.intro = intro;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setPoint(int point){this.point=point;}

    public ProfileData(String userId,Bitmap profile_image, int feed_counter, int follow_counter,
                       int follower_counter, String intro, String url,int point){
        this.userId = userId;
        this.profile_image = profile_image;
        this.feed_counter = feed_counter;
        this.follow_counter = follow_counter;
        this.follower_counter = follower_counter;
        this.intro=intro;
        this.url=url;
        this.point=point;
    }
}
