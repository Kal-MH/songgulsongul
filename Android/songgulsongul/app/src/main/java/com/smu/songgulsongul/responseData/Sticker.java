package com.smu.songgulsongul.responseData;

public class Sticker {
    public Sticker(int id, String image, String name, int price){
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    int id;
    String image;
    String name;
    int price;
    String text;

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getText() {
        return text;
    }
}
