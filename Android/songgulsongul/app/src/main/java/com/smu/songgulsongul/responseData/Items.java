package com.smu.songgulsongul.responseData;

import com.google.gson.annotations.SerializedName;
import com.smu.songgulsongul.item.ItemSearchItem;

public class Items {

    @SerializedName("title") String title = "";

    @SerializedName("link") String link= "";

    @SerializedName("image") String img= "";

    @SerializedName("lprice") String lprice= "";

    @SerializedName("hprice") String hprice= "";

    @SerializedName("mallName") String mallName = "";

    @SerializedName("productId") String productId= "";

    @SerializedName("productType") String productType= "";


    @SerializedName("brand") String brand = "";


    @SerializedName("category1") String category1 ="";

    @SerializedName("category2") String category2="";

    @SerializedName("category3") String category3="";

    @SerializedName("category4") String category4="";



    @Override
    public String toString() {
        return "ItemSearchData{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", img='" + img + '\'' +
                ", lprice=" + lprice +
                ", hprice=" + hprice +
                ", mallName='" + mallName + '\'' +
                ", productId=" + productId +
                ", productType=" + productType +
                ", brand='" + brand + '\'' +
                ", category1='" + category1 + '\'' +
                ", category2='" + category2 + '\'' +
                ", category3='" + category3 + '\'' +
                ", category4='" + category4 + '\'' +
                '}';
    }
}
