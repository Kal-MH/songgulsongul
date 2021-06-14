package com.smu.songgulsongul.responseData;

public class ItemTag {
    int id;
    int post_id;
    String name;
    String hprice;
    String lprice;
    String url;
    String picture;
    String brand;
    String category1;
    String category2;

    // post upload test용 생성자 --> 추후 삭제
    public ItemTag(int id, String name, String h_price, String l_price, String url, String picture, String brand, String category1, String category2){
        this.name = name;
        this.hprice = h_price;
        this.lprice = l_price;
        this.url = url;
        this.picture = picture;
        this.brand = brand;
        this.category1 = category1;
        this.category2 = category2;
    }

    public ItemTag(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getName() {
        return name;
    }

    public String getH_price() {
        return hprice;
    }

    public String getL_price() {
        return lprice;
    }

    public String getUrl() {
        return url;
    }

    public String getPicture() {
        return picture;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }
}
