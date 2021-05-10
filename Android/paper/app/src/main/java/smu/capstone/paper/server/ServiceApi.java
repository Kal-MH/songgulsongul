package smu.capstone.paper.server;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import smu.capstone.paper.data.CodeResponse;

import smu.capstone.paper.data.FollowData;
import smu.capstone.paper.data.FollowListData;
import smu.capstone.paper.data.EmailData;
import smu.capstone.paper.data.FindData;
import smu.capstone.paper.data.IdCheckData;
import smu.capstone.paper.data.IdData;
import smu.capstone.paper.data.JoinData;
import smu.capstone.paper.data.LoginData;
import smu.capstone.paper.data.UserData;
import smu.capstone.paper.data.LoginResponse;

public interface ServiceApi {
    // 아이디 중복체크
    @POST("/api/dup-idcheck")
    Call<CodeResponse> IdCheck(@Body IdCheckData data);

    // 인증 이메일 보내기
    @POST("/api/email-auth")
    Call<JsonObject> EmailAuth(@Body EmailData data);

    //좋아요
    @GET("/api/like")
    Call<CodeResponse> Like(@Query("userid") int userid, @Query("postid") int postid);

    //보관하기
    @GET("/api/keep")
    Call<CodeResponse> Keep(@Query("userid") int userid, @Query("postid") int postid);

    // 회원가입
    @POST("/join")
    Call<CodeResponse> Join(@Body JoinData data);
  
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

    //로그인
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

    //피드 게시글 가져오기
    @GET("/post/feeds")
    Call<JsonObject> GetFeed(@Query("userid") int id, @Query("offset") int offset );

    //커뮤니트 게시글 가져오기
    @GET("/post/community")
    Call<JsonObject> GetCommunity(@Query("offset") int offset);

    //세부 게시글 내용 가져오기
    @GET("/post/{id}")
    Call<JsonObject> GetDetailPost(@Path("id") int id, @Query("userid") int userid);

}
