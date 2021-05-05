package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class FollowListData {
    @SerializedName("id")
    private String id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("status")
    private int status;

    public FollowListData(String id){
        this.id = id;
    }
    public void addUserId(String user_id){
        this.user_id = user_id;
    }
    public void addStatus(int status){
        this.status = status;
    }
}
