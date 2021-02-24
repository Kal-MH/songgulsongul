package smu.capstone.paper.item;

public class PostItem {
    int img;
    int pid;

    public PostItem(int img){
        this.img = img;
    }

    public PostItem(int img, int pid){
        this.img = img;
        this.pid = pid;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
