package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class JoinData {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("login_id")
    private String login_id;

    @SerializedName("sns_url")
    private String sns_url;

    public JoinData(String email, String password, String login_id, String sns_url) {
        this.email = email;
        this.password = password;
        this.login_id = login_id;
        this.sns_url = sns_url;
    }
}