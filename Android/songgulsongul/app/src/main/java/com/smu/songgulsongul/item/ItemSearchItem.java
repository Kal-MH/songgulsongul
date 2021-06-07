package com.smu.songgulsongul.item;

public class ItemSearchItem {
    String picurl;
    int id;
    int post_id;
    String name;
    String hprice;
    String lprice;

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
}
