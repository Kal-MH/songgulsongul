package smu.capstone.paper.responseData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("followerCnt")
    private int followerCnt;

    @SerializedName("followCnt")
    private int followCnt;

    @SerializedName("postInfo")
    private List<Post> postInfo;

    @SerializedName("profileInfo")
    private User profileInfo;

    public int getCode() {
        return code;
    }

    public int getFollowerCnt() {
        return followerCnt;
    }

    public int getFollowCnt() {
        return followCnt;
    }

    public List<Post> getPostInfo() {
        return postInfo;
    }

    public User getProfileInfo() {
        return profileInfo;
    }
}
