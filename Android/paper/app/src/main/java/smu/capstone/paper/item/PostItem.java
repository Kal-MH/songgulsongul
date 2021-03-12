package smu.capstone.paper.item;

import android.graphics.Bitmap;

public class PostItem {
    int img;
    int pid;
    String userId;
    Bitmap profile_image;
    String timeStamp;
    int favoriteCounter;
    int commentCounter;
    int like;
    int keep;
    int ccl_cc;
    int ccl_a;
    int ccl_nc;
    int ccl_nd;
    int ccl_sa;
    String text;
    int postId;

    public PostItem(int img){
        this.img = img;
    }

    public PostItem(int img, int pid){
        this.img = img;
        this.pid = pid;
    }
    public PostItem(String userId, String timeStamp, int favoriteCounter, int commentCounter, String text , int like
            , Bitmap profile_image, int keep, int ccl_cc, int ccl_a, int ccl_nc, int ccl_nd, int ccl_sa) {
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.favoriteCounter = favoriteCounter;
        this.commentCounter=commentCounter;
        this.text=text;
        this.like = like;
        this.profile_image = profile_image;
        this.keep = keep;
        this.ccl_cc = ccl_cc;
        this.ccl_a = ccl_a;
        this.ccl_nc = ccl_nc;
        this.ccl_nd = ccl_nd;
        this.ccl_sa = ccl_sa;
    }

    public int getImg() {
        return img;
    }
    public int getPid() {
        return pid;
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
    public String getUserId() {
        return userId;
    }
    public int getLike() {
        return like;
    }
    public int getKeep() {
        return keep;
    }
    public int getCcl1(){return ccl_cc;}
    public int getCcl2(){return ccl_a;}
    public int getCcl3(){return ccl_nc;}
    public int getCcl4(){return ccl_nd;}
    public int getCcl5(){return ccl_sa;}
    public int getPostId(){return postId;}

    public void setImg(int img) {
        this.img = img;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
    public void setCommentCounter(int commentCounter) {
        this.commentCounter = commentCounter;
    }
    public void setProfile_image(Bitmap profile_image) {
        this.profile_image = profile_image;
    }
    public void setFavoriteCounter(int favoriteCounter) {
        this.favoriteCounter = favoriteCounter;
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
    public void setCcl1(int ccl1) {this.ccl_cc = ccl_cc;}
    public void setCcl2(int ccl2) {this.ccl_a = ccl_a;}
    public void setCcl3(int ccl3) {this.ccl_nc = ccl_nc;}
    public void setCcl4(int ccl4) {this.ccl_nd = ccl_nd;}
    public void setCcl5(int ccl5) {this.ccl_sa = ccl_sa;}
}
