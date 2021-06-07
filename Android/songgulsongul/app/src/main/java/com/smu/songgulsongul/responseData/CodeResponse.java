package com.smu.songgulsongul.responseData;

import com.google.gson.annotations.SerializedName;

public class CodeResponse {
    @SerializedName("code")
    private int code;

    public CodeResponse(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
