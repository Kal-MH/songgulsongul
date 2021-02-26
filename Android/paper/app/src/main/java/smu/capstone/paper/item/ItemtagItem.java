package smu.capstone.paper.item;

import android.graphics.Bitmap;


public class ItemtagItem {
    Bitmap pic;
    int cost;
    String name;
    String company;

    public ItemtagItem(Bitmap pic, int cost, String name, String company) {
        this.pic = pic;
        this.cost = cost;
        this.name = name;
        this.company = company;
    }


    public void setCompany(String company) {
        this.company = company;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public Bitmap getPic() {
        return pic;
    }

    public int getCost() {
        return cost;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }
}
