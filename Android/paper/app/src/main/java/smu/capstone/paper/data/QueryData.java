package smu.capstone.paper.data;

import android.app.DownloadManager;

import com.google.gson.annotations.SerializedName;

public class QueryData {
    @SerializedName("query")
    String query;

    public QueryData(String query){
        this.query = query;
    }
}
