package smu.capstone.paper.responseData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private List<PostComu> data;

    public void addData(List<PostComu> p){
        for (PostComu postComu : p)
            data.add(postComu);
    }

    public int getCode() {
        return code;
    }

    public List<PostComu> getData() {
        return data;
    }
}
