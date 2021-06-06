package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("id")
    String id;

    @SerializedName("user_id")
    String user_id;

    @SerializedName("status")
    int status;

    public UserData(String id, int status){
        this.id = id;
        this.status = status;
    }

    public void SetUserId(String user_id){
        this.user_id = user_id;
    }
}
