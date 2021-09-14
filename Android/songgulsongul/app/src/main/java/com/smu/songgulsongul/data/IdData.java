package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class IdData {
    @SerializedName("id")
    int id;

    public IdData(int id) {
        this.id = id;
    }
}
