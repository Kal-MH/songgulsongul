package smu.capstone.paper.responseData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarketDetailResponse {
    @SerializedName("code")
    int code;

    @SerializedName("stickerDetail")
    Sticker stickerDetail;

    @SerializedName("sellerInfo")
    User sellerInfo;

    @SerializedName("userPoint")
    int userPoint;

    public int getCode() {
        return code;
    }

    public Sticker getStickerDetail() {
        return stickerDetail;
    }

    public User getSellerInfo() {
        return sellerInfo;
    }

    public int getUserPoint() {
        return userPoint;
    }
}
