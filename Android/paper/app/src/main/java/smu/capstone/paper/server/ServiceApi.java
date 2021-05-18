package smu.capstone.paper.server;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import smu.capstone.paper.data.CodeResponse;

import smu.capstone.paper.data.FollowData;
import smu.capstone.paper.data.FollowListData;
import smu.capstone.paper.data.EmailData;
import smu.capstone.paper.data.FindData;
import smu.capstone.paper.data.IdCheckData;
import smu.capstone.paper.data.IdData;
import smu.capstone.paper.data.JoinData;
import smu.capstone.paper.data.KeepData;
import smu.capstone.paper.data.LoginData;
import smu.capstone.paper.data.ProfileEditData;
import smu.capstone.paper.data.UserData;
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
    Call<JsonObject> Keep(@Body KeepData data);

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
    Call<JsonObject> GetFeed(@Query("userid") int id, @Query("offset") int offset);

    //기존 프로필 데이터 불러오기
    @POST("user/profile-data")
    Call<JsonObject> ProfileData(@Body UserData data);

    //프로필 수정
    @Multipart
    @POST("/user/profile-edit")
    Call<CodeResponse> EditProfile(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part File);

    //회원 탈퇴
    @POST("/user/data-delete")
    Call<CodeResponse> DeleteAccount(@Body UserData data);

}
