package com.smu.songgulsongul.item;

import com.google.gson.annotations.SerializedName;

public class ItemSearchItem {


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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLprice() {
        return lprice;
    }

    public void setLprice(String lprice) {
        this.lprice = lprice;
    }

    public String getHprice() {
        return hprice;
    }

    public void setHprice(String hprice) {
        this.hprice = hprice;
    }

    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getCategory4() {
        return category4;
    }

    public void setCategory4(String category4) {
        this.category4 = category4;
    }
}
