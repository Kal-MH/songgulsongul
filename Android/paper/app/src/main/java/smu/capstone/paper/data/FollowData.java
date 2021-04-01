package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class FollowData {
    @SerializedName("loginId")
    private String loginId;

    @SerializedName("userId")
    private String userId;

    public FollowData(String loginId, String userId){
        this.loginId = loginId;
        this.userId = userId;
    }
}
