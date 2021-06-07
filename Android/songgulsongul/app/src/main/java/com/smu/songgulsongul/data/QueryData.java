package com.smu.songgulsongul.data;

import com.google.gson.annotations.SerializedName;

public class QueryData {
    @SerializedName("query")
    String query;

    public QueryData(String query){
        this.query = query;
    }
}
