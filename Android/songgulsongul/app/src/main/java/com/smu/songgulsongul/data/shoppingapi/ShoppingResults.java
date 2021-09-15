package com.smu.songgulsongul.data.shoppingapi;

import com.google.gson.annotations.SerializedName;
import com.smu.songgulsongul.recycler_item.ItemSearchItem;

import java.util.List;

public class ShoppingResults {
    @SerializedName("title")
    String title = "";

    @SerializedName("link")
    String link = "";

    @SerializedName("description")
    String description = "";

    @SerializedName("lastBuildDate")
    String lastBuildDate = "";

    @SerializedName("total")
    String total;

    @SerializedName("start")
    Integer start = 0;

    @SerializedName("display")
    String display;

    @SerializedName("items")
    List<ItemSearchItem> items;


    public List<ItemSearchItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        String str = "";
        for (ItemSearchItem data : items)
            str += data.toString();
        return "ShoppingResults{" +
                "items=" + items.toString() + "\n" +
                str + "\n" +
                '}';
    }
}
