package smu.capstone.paper.responseData;

public class Post {
    int id;
    String image;
    String text;
    String post_time;
    String post_date;

    Ccl ccl;

    public Ccl getCcl() {
        return ccl;
    }

    public Post(int id, String image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public String getPost_time() {
        return post_time;
    }

    public String getPost_date() {
        return post_date;
    }

}