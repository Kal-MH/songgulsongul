package smu.capstone.paper.server;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import smu.capstone.paper.data.JoinData;

public interface ServiceApi {
    // 아이디 중복체크
    @POST("/api/dup-idcheck")
    Call<JSONObject> IdCheck(@Body String login_id);

    // 인증 이메일 보내기
    @POST("/api/email-auth")
    Call<JSONObject> EmailAuth(@Body String email);

    // 회원가입
    @POST("/join")
    Call<JSONObject> Join(@Body JoinData data);

}
