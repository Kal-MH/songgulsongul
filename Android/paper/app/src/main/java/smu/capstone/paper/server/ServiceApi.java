package smu.capstone.paper.server;

import android.app.DownloadManager;

import com.google.gson.JsonObject;


import java.util.List;
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
import retrofit2.http.Path;
import retrofit2.http.Query;
import smu.capstone.paper.responseData.CodeResponse;

import smu.capstone.paper.data.CommentData;
import smu.capstone.paper.data.FollowData;
import smu.capstone.paper.data.FollowListData;
import smu.capstone.paper.data.EmailData;
import smu.capstone.paper.data.FindData;
import smu.capstone.paper.data.IdCheckData;
import smu.capstone.paper.data.IdData;
import smu.capstone.paper.data.JoinData;
import smu.capstone.paper.data.KeepData;
import smu.capstone.paper.data.LoginData;

import smu.capstone.paper.responseData.KeepResponse;
import smu.capstone.paper.responseData.Post;
import smu.capstone.paper.responseData.PostListResponse;
import smu.capstone.paper.responseData.PostFeedResponse;

import smu.capstone.paper.data.UserData;
import smu.capstone.paper.responseData.LoginResponse;
import smu.capstone.paper.responseData.PostResponse;
import smu.capstone.paper.responseData.ProfileResponse;
import smu.capstone.paper.responseData.SearchIdResponse;

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

    //댓글 작성
    @POST("/api/comment")
    Call<CodeResponse> Comment(@Body CommentData commentData);

    //댓글 삭제
    @GET("/api/comment/delete")
    Call<CodeResponse> DeleteComment(@Query("postid") int postid, @Query("commentid") int commentid );

    // 회원가입
    @POST("/join")
    Call<CodeResponse> Join(@Body JoinData data);
  
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
    Call<KeepResponse> Keep(@Body KeepData data);

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
    Call<PostFeedResponse> GetFeed(@Query("userid") int id, @Query("offset") int offset );
 
    // 프로필
    @POST("/user/profile")
    Call<ProfileResponse> Profile(@Body UserData data);

    //프로필 수정
    @Multipart
    @POST("/user/profile-edit")
    Call<CodeResponse> EditProfile(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part File);

    //회원 탈퇴
    @POST("/user/data-delete")
    Call<CodeResponse> DeleteAccount(@Body UserData data);


    //커뮤니트 게시글 가져오기
    @GET("/post/community")
    Call<PostListResponse> GetCommunity(@Query("offset") int offset);

    //세부 게시글 내용 가져오기
    @GET("/post/{id}")
    Call<PostResponse> GetDetailPost(@Path("id") int id, @Query("userid") int userid);

    // 검색
    @GET("/post/search/id")
    Call<SearchIdResponse> SearchPostId(@Query("keyword") String keyword , @Query("offset") int offset);

    @GET("/post/search/tag")
    Call<PostListResponse> SearchPostTag(@Query("keyword") String keyword , @Query("offset") int offset);

    // 게시글 업로드
    @Multipart
    @POST("/post/upload")
    Call<JsonObject> PostUpload(
            @Part("user_id") RequestBody id,
            @Part("text") RequestBody text,
            @Part List<MultipartBody.Part> hashTags,
            @Part List<MultipartBody.Part> ccl,
            @Part List<MultipartBody.Part> itemTags,
            @Part MultipartBody.Part postImg);

    // 게시글 삭제
    @GET("/post/delete")
    Call<CodeResponse> PostDelete(@Query("userid") int user_id, @Query("postid") int post_id);
}
