package smu.capstone.paper.item;

import android.graphics.Bitmap;

public class HomeFeedItem {
    Bitmap profile_image;
    private String userId;
    private String timeStamp;
    Bitmap picture;
    private int favoriteCounter;
    private int commentCounter;
    private String text;
    private int like;
    private int keep;
    private int postId;

    public String getId(){
        return userId;
    }
    public String getTimeStamp(){
        return timeStamp;
    }
    public int getFavoriteCounter(){
        return favoriteCounter;
    }
    public int getCommentCounter(){
        return commentCounter;
    }
    public String getText(){
        return text;
    }
    public Bitmap getProfile_image() {
        return profile_image;
    }
    public Bitmap getPicture() {
        return picture;
    }
    public String getUserId() {
        return userId;
    }
    public int getLike() {
        return like;
    }
    public int getKeep() {
        return keep;
    }
    public int getPostId(){return postId;}

    public void setCommentCounter(int commentCounter) {
        this.commentCounter = commentCounter;
    }
    public void setProfile_image(Bitmap profile_image) {
        this.profile_image = profile_image;
    }
    public void setFavoriteCounter(int favoriteCounter) {
        this.favoriteCounter = favoriteCounter;
    }
    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setLike(int like) {
        this.like = like;
    }
    public void plusLike(){
        this.like+=1;
    }
    public void minusLike(){
        this.like-=1;
    }
    public void setKeep(int keep) {this.keep = keep;}
    public HomeFeedItem(String userId, String timeStamp, int favoriteCounter, int commentCounter, String text , int like
                            , Bitmap picture, Bitmap profile_image, int keep) {
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.favoriteCounter = favoriteCounter;
        this.commentCounter=commentCounter;
        this.text=text;
        this.like = like;
        this.picture = picture;
        this.profile_image = profile_image;
        this.keep = keep;
    }
    public HomeFeedItem(String userId, String timeStamp, int favoriteCounter, int commentCounter, String text) {
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.favoriteCounter = favoriteCounter;
        this.commentCounter=commentCounter;
        this.text=text;
    }
}
