package smu.capstone.paper.data;

import android.widget.ImageView;
import android.widget.TextView;

public class FeedData {
    ImageView profile_image;
    private String userId;
    private String timeStamp;
    ImageView picture;
    private int favoriteCounter;
    private int commentCounter;
    private String text;

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

    public FeedData(String userId, String timeStamp, int favoriteCounter, int commentCounter, String text) {
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.favoriteCounter = favoriteCounter;
        this.commentCounter=commentCounter;
        this.text=text;
    }
}
