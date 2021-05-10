package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("id")
    private int id;

    public LoginResponse(int code, int id) {
        this.code = code;
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public int getId() {
        return id;
    }
}