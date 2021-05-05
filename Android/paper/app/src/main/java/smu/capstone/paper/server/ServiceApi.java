package smu.capstone.paper.server;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import smu.capstone.paper.data.CodeResponse;
import smu.capstone.paper.data.EmailData;
import smu.capstone.paper.data.FindData;
import smu.capstone.paper.data.IdCheckData;
import smu.capstone.paper.data.IdData;
import smu.capstone.paper.data.JoinData;
import smu.capstone.paper.data.LoginData;
import smu.capstone.paper.data.LoginResponse;

public interface ServiceApi {
    // 아이디 중복체크
    @POST("/api/dup-idcheck")
    Call<CodeResponse> IdCheck(@Body IdCheckData data);

    // 인증 이메일 보내기
    @POST("/api/email-auth")
    Call<JsonObject> EmailAuth(@Body EmailData data);

    // 회원가입
    @POST("/join")
    Call<CodeResponse> Join(@Body JoinData data);

    // 로그인
    @POST("/login")
    Call<LoginResponse> Login(@Body LoginData data);

    //출석체크 포인트
    @POST("/api/point/attendance")
    Call<CodeResponse> Attendance(@Body IdData data);

    // 아이디 찾기
    @POST("/find/id")
    Call<CodeResponse> FindId(@Body EmailData data);

    // 비밀번호 찾기
    @POST("/find/password")
    Call<CodeResponse> FindPw(@Body FindData data);
}
