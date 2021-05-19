package smu.capstone.paper.responseData;

import com.google.gson.annotations.SerializedName;


public class PostResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private PostDetail data;

    public int getCode() {
        return code;
    }

    public PostDetail getData() {
        return data;
    }
}
