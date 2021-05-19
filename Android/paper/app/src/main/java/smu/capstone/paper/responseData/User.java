package smu.capstone.paper.responseData;

public class User {
    public User(int user_id, String login_id, String img_profile) {
        this.user_id = user_id;
        this.login_id = login_id;
        this.img_profile = img_profile;
    }

    int user_id;
    String login_id;
    String img_profile;

    public int getUser_id() {
        return user_id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public String getImg_profile() {
        return img_profile;
    }
}