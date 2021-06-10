package com.smu.songgulsongul.server;

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
import com.smu.songgulsongul.data.PostEditData;
import com.smu.songgulsongul.data.PwEditData;
import com.smu.songgulsongul.responseData.CodeResponse;

import com.smu.songgulsongul.data.CommentData;
import com.smu.songgulsongul.data.FollowData;
import com.smu.songgulsongul.data.FollowListData;
import com.smu.songgulsongul.data.EmailData;
import com.smu.songgulsongul.data.FindData;
import com.smu.songgulsongul.data.IdCheckData;
import com.smu.songgulsongul.data.IdData;
import com.smu.songgulsongul.data.JoinData;
import com.smu.songgulsongul.data.KeepData;
import com.smu.songgulsongul.data.LoginData;

import com.smu.songgulsongul.responseData.KeepResponse;
import com.smu.songgulsongul.responseData.MarketDetailResponse;
import com.smu.songgulsongul.responseData.MarketResponse;
import com.smu.songgulsongul.responseData.PostListResponse;
import com.smu.songgulsongul.responseData.PostFeedResponse;

import com.smu.songgulsongul.data.UserData;
import com.smu.songgulsongul.responseData.LoginResponse;
import com.smu.songgulsongul.responseData.PostResponse;
import com.smu.songgulsongul.responseData.ProfileResponse;
import com.smu.songgulsongul.responseData.SearchIdResponse;

public interface ServiceApi {
    // 아이디 중복체크
    @POST("/api/dup-idcheck")
    Call<CodeResponse> IdCheck(@Body IdCheckData data);

    // 인증 이메일 보내기
    @POST("/api/email-auth")
    Call<JsonObject> EmailAuth(@Body EmailData data);

    // 아이디 변경
    @GET("/user/id-change")
    Call<CodeResponse> IdChange(@Query("login_id") String login_id, @Query("new_id") String new_id);

    // 비밀번호 확인
    @POST("/api/check/password")
    Call<CodeResponse> PwCheck(@Body PwEditData data);

    // 비밀번호 변경
    @POST("/user/pw-change")
    Call<CodeResponse> PwChange(@Body PwEditData data);

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
    Call<PostFeedResponse> GetFeed(@Query("userid") int id, @Query("offset") Integer offset );
 
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
    Call<PostListResponse> GetCommunity(@Query("offset") Integer offset);

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

    // 게시글 수정
    @POST("/post/update")
    Call<CodeResponse> PostUpdate(@Body PostEditData data);

    // 게시글 삭제
    @GET("/post/delete")
    Call<CodeResponse> PostDelete(@Query("userid") int user_id, @Query("postid") int post_id);

    // 마켓 메인
    @GET("/market/main")
    Call<MarketResponse> MarketMain(@Query("offset") Integer offset);

    // 마켓 스티커 디테일
    @GET("/market/detail")
    Call<MarketDetailResponse> StickerDetail(@Query("sticker_id") int sticker_id, @Query("user_id") int user_id);

    // 마켓 스티커 구매
    @GET("/market/buy")
    Call<JsonObject> StickerBuy(@Query("sticker_id") int sticker_id, @Query("user_id") int user_id);

    // 스티커 검색
    @GET("/market/sticker-search")
    Call<MarketResponse> StickerSearch(@Query("search_word") String search_word, @Query("offset") int offset);

    // 스티커 검색 - 낮은 가격순
    @GET("/market/search-price")
    Call<MarketResponse> SearchPrice(@Query("search_word") String search_word, @Query("offset") int offset);

    // 스티커 검색 - 최신순
    @GET("/market/search-date")
    Call<MarketResponse> SearchDate(@Query("search_word") String search_word, @Query("offset") int offset);

    @GET("/notification/pushalarm")
    Call<CodeResponse> PushAlarm(@Query("token")String token);
}
