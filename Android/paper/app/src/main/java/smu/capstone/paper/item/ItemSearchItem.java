package smu.capstone.paper.item;

public class ItemSearchItem {
    int id;
    int post_id;
    String name;
    int hprice;
    int lprice;

    public ItemSearchItem(int id, int post_id, String name, int hprice, int lprice){
        this.id = id;
        this.post_id = post_id;
        this.name = name;
        this.hprice = hprice;
        this.lprice = lprice;
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

    public void setHprice(int hprice){
        this.hprice = hprice;
    }

    public void setLprice(int lprice){
        this.lprice = lprice;
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

    public int getHprice(){
        return hprice;
    }

    public int getLprice(){
        return lprice;
    }
}
