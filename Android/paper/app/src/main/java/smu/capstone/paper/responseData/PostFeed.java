package smu.capstone.paper.responseData;

public class PostFeed {
    Post post;
    User user;
    int commentsNum;

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public void setLikeOnset(int likeOnset) {
        this.likeOnset = likeOnset;
    }

    public void setKeepOnset(int keepOnset) {
        this.keepOnset = keepOnset;
    }

    public int getLikeOnset() {
        return likeOnset;
    }

    public int getKeepOnset() {
        return keepOnset;
    }

    public PostFeed(){

    }
    public PostFeed(Post post, User user, int commentsNum, int likeNum, int likeOnset, int keepOnset) {
        this.post = post;
        this.user = user;
        this.commentsNum = commentsNum;
        this.likeNum = likeNum;
        this.likeOnset = likeOnset;
        this.keepOnset = keepOnset;
    }

    int likeNum;
    int likeOnset;
    int keepOnset;
}

