package smu.capstone.paper.data;

import com.google.gson.annotations.SerializedName;

public class EmailAuthData {
    @SerializedName("email")
    String email;

    public EmailAuthData(String email){
        this.email = email;
    }

}
