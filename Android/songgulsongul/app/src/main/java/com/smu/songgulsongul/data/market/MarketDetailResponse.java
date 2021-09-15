package com.smu.songgulsongul.data.market;

import com.google.gson.annotations.SerializedName;
import com.smu.songgulsongul.recycler_item.User;
import com.smu.songgulsongul.recycler_item.Sticker;

import java.util.List;

public class MarketDetailResponse {
    @SerializedName("code")
    int code;

    @SerializedName("stickerDetail")
    List<Sticker> stickerDetail;

    @SerializedName("sellerInfo")
    List<User> sellerInfo;

    @SerializedName("userPoint")
    int userPoint;

    public int getCode() {
        return code;
    }

    public List<Sticker> getStickerDetail() {
        return stickerDetail;
    }

    public List<User> getSellerInfo() {
        return sellerInfo;
    }

    public int getUserPoint() {
        return userPoint;
    }
}
