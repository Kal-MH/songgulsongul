package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class ProfileEditData {
    @SerializedName("id") // 기존 id
    String id;

    @SerializedName("newId") // 변경된 id
    String newId;

    @SerializedName("newIntro")
    String newIntro;

    @SerializedName("newSNS")
    String newSNS;

    @SerializedName("profileImage")
    int profileImage; // 실제 image url로 전달

    public ProfileEditData(String id, String newId, String newIntro, String newSNS, int profileImage){
        this.id = id;
        this.newId = newId;
        this.newIntro = newIntro;
        this.newSNS = newSNS;
        this.profileImage = profileImage;
    }
}
