package com.smu.songgulsongul.data.market;

import com.google.gson.annotations.SerializedName;
import com.smu.songgulsongul.recycler_item.Sticker;

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
