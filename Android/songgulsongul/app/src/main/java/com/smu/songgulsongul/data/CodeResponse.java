package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class CodeResponse {
    @SerializedName("code")
    private final int code;

    public CodeResponse(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
