package com.smu.songgulsongul.responseData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarketResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("marketItem")
    private List<Sticker> marketItem;

    public int getCode() {
        return code;
    }

    public List<Sticker> getMarketItem() {
        return marketItem;
    }
}
