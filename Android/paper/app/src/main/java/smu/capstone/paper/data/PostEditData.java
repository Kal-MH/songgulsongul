package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class PostEditData {
    @SerializedName("postId")
    int postId;

    @SerializedName("text") // 글 내용
    String text;

    @SerializedName("hashTag") // 해쉬태그
    String hasTag[];

    //@SerializedName("itemTag") // 아이템태그(클래스 생성 고려 필요)

    @SerializedName("ccl") // ccl설정값
    int ccl[];

    public PostEditData(int postId, String text, String hashTag[], int ccl[]){
        this.postId = postId;
        this.text = text;
        this.hasTag = hashTag;
        this.ccl = ccl;
    }
}
