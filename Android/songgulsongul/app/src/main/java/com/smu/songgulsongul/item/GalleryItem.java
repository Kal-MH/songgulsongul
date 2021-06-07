package com.smu.songgulsongul.item;

public class GalleryItem {

    String path;
    String name;

    public GalleryItem(String path){
        this.path = path;
    }

    public GalleryItem(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }


}
