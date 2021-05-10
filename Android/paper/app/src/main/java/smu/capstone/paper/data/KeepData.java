package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class KeepData {
    @SerializedName("login_id")
    String login_id;

    public KeepData(String login_id){
        this.login_id = login_id;
    }

}
