package smu.capstone.paper.responseData;

public class ItemTag {
    int id;
    int post_id;
    String name;
    int h_price;
    int l_price;
    String url;
    String picture;
    String brand;
    String category1;
    String category2;

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

    public int getH_price() {
        return h_price;
    }

    public int getL_price() {
        return l_price;
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
