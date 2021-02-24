package smu.capstone.paper.item;

import android.graphics.Bitmap;

public class FollowItem {
    private boolean following;
    private String id;
    private Bitmap image;

    public FollowItem(boolean following, String id, Bitmap image){
        this.following = following;
        this.id = id;
        this.image = image;
    }


    public void setFollowing(boolean following) {
        this.following = following;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public boolean getFollowing(){return following;}


}
