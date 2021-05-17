package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class ProfileEditData {
    @SerializedName("id_check_flag")
    int id_check_flag;

    @SerializedName("sns_check_flag")
    int sns_check_flag;

    @SerializedName("img_check_flag")
    int img_check_flag;

    @SerializedName("login_id") // 기존 id
    String login_id;

    @SerializedName("new_id") // 변경된 id
    String new_id;

    @SerializedName("new_intro")
    String new_intro;

    @SerializedName("new_SNS")
    String new_SNS;

    public ProfileEditData(int id_check_flag, int sns_check_flag, int img_check_flag, String login_id, String new_intro, String new_SNS){
        this.id_check_flag = id_check_flag;
        this.sns_check_flag = sns_check_flag;
        this.img_check_flag = img_check_flag;
        this.login_id = login_id;
        this.new_intro = new_intro;
        this.new_SNS = new_SNS;
    }

    public void setNewId(String new_id){
        this.new_id = new_id;
    }
}
