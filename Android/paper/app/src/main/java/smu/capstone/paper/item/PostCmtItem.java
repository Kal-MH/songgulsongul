package smu.capstone.paper.item;

public class PostCmtItem {
    String user_id;
    String cmt;

    public PostCmtItem() {

    }

    public void setId(String user_id) { this.user_id = user_id ; }
    public void setCmt(String cmt) { this.cmt = cmt ; }

    public String getId() { return user_id ; }
    public String getCmt() { return cmt; }

    public PostCmtItem(String user_Id, String cmt){
        this.user_id = user_id;
        this.cmt=cmt;
    }
}
