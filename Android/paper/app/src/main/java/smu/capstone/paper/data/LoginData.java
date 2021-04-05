package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("login_id")
    String login_id;

    @SerializedName("password")
    String password;

    public LoginData(String login_id, String password) {
        this.login_id = login_id;
        this.password = password;
    }
}