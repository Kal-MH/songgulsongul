package smu.capstone.paper.server;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import smu.capstone.paper.data.CodeResponse;
import smu.capstone.paper.data.EmailAuthData;
import smu.capstone.paper.data.FollowData;
import smu.capstone.paper.data.FollowListData;
import smu.capstone.paper.data.IdCheckData;
import smu.capstone.paper.data.JoinData;
import smu.capstone.paper.data.LoginData;
import smu.capstone.paper.data.UserData;

public interface ServiceApi {
    // 아이디 중복체크
    @POST("/api/dup-idcheck")
    Call<CodeResponse> IdCheck(@Body IdCheckData data);

    // 인증 이메일 보내기
    @POST("/api/email-auth")
    Call<JsonObject> EmailAuth(@Body EmailAuthData data);

    // 회원가입
    @POST("/join")
    Call<CodeResponse> Join(@Body JoinData data);

    // 로그인
    @POST("/login")
    Call<CodeResponse> Login(@Body LoginData data);

    // 프로필
    @POST("/user/profile")
    Call<JsonObject> Profile(@Body UserData data);

    // 팔로우하기
    @POST("/user/follow")
    Call<CodeResponse> Follow(@Body FollowData data);

    // 언팔로우하기
    @POST("/user/unfollow")
    Call<CodeResponse> UnFollow(@Body FollowData data);

    // 팔로우 목록 받아오기
    @POST("/user/lfollow-list")
    Call<JsonObject> LFollowList(@Body FollowListData data);

    // 선택한 사용자의 팔로우 목록 받아오기
    @POST("/user/follow-list")
    Call<JsonObject> FollowList(@Body FollowListData data);

    // 팔로워 목록 받아오기
    @POST("/user/follower-list")
    Call<JsonObject> FollowerList(@Body FollowListData data);

    // 보관함
    @POST("/user/keep")
    Call<JsonObject> Keep(@Body UserData data);
}
