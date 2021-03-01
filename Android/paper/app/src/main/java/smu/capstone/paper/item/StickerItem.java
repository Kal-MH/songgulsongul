package smu.capstone.paper.item;

public class StickerItem {
    private int img;
    private String sname;
    private String sprice;

    public StickerItem(int img, String sname, String sprice){
        this.img = img;
        this.sname = sname;
        this.sprice = sprice;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }
}
