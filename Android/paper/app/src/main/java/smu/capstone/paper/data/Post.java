package smu.capstone.paper.data;


public class Post {
    public Post(int id, String image, String text, String post_time, String post_date) {
        this.id = id;
        this.image = image;
        this.text = text;
        this.post_time = post_time;
        this.post_date = post_date;
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

    int id;
    String image;
    String text;
    String post_time;
    String post_date;
}