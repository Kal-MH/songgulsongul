package com.smu.songgulsongul.item;

public class ItemSearchItem {
    String picurl;
    int id;
    int post_id;
    String name;
    String hprice;
    String lprice;
    String url;
    String brand;
    String category1;
    String category2;

    public void setPic(String picurl) {
        this.picurl = picurl;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setPost_id(int post_id){
        this.post_id = post_id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHprice(String hprice){
        this.hprice = hprice;
    }

    public void setLprice(String lprice){
        this.lprice = lprice;
    }

    public void setUrl(String url) { this.url = url; }

    public void setBrand(String brand) { this.brand = brand; }

    public void setCategory1(String category1) { this.category1 = category1; }

    public void setCategory2(String category2) { this.category2 = category2; }


    public String getPic(){
        return picurl;
    }

    public int getId(){
        return id;
    }

    public int getPost_id(){
        return post_id;
    }

    public String getName(){
        return name;
    }

    public String getHprice(){
        return hprice;
    }

    public String getLprice(){
        return lprice;
    }

    public String getUrl(){
        return url;
    }

    public String getBrand() { return brand; }

    public String getCategory1() { return category1; }

    public String getCategory2() { return category2; }
}
