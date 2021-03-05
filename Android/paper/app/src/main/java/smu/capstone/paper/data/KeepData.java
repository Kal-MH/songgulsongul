package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class KeepData {
    @SerializedName("loginId")
    String loginId;

    @SerializedName("postId")
    int postId;

    public KeepData(String loginId, int postId){
        this.loginId = loginId;
        this.postId = postId;
    }

}
