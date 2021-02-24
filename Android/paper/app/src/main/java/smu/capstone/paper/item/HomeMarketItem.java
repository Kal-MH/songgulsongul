package smu.capstone.paper.item;

public class HomeMarketItem{
    int img;
    String iname;
    String icost;

    public HomeMarketItem(int img, String iname, String icost){
        this.img = img;
        this.iname = iname;
        this.icost = icost;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public String getIcost() {
        return icost;
    }

    public void setIcost(String icost) {
        this.icost = icost;
    }
}
