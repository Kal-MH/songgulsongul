package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import java.util.List;
import java.lang.ref.ReferenceQueue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.responseData.CodeResponse;
import smu.capstone.paper.data.FollowData;
import smu.capstone.paper.data.UserData;
import smu.capstone.paper.responseData.Post;
import smu.capstone.paper.responseData.ProfileResponse;
import smu.capstone.paper.responseData.User;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;


public class ProfileActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;

    final int MY = 1;
    final int OTHER = 0;

    public int Status;
    public static final int REQUEST_CODE = 100;

    String login_id;
    String user_id;

    private GridView gridView;
    private PostImageAdapter adapter;
    private TextView feed_count_tv, follow_count_tv, follower_count_tv, points_tv, intro_tv, sns_tv;
    private Button follow_btn, unfollow_btn;
    private LinearLayout profile_follows;
    private LinearLayout pointview;

    private List<Post> post_data;
    private User user_data;
    private ImageView profile_userimage;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // Find view by ID
        feed_count_tv = findViewById(R.id.profile_feed_cnt);
        follow_count_tv = findViewById(R.id.profile_follow_cnt);
        follower_count_tv = findViewById(R.id.profile_follower_cnt);
        points_tv = findViewById(R.id.profile_point);
        intro_tv = findViewById(R.id.profile_intro);
        sns_tv = findViewById(R.id.profile_snsurl);
        follow_btn = findViewById(R.id.profile_follow_btn);
        unfollow_btn = findViewById(R.id.profile_unfollow_btn);
        pointview = findViewById(R.id.profile_pointview);
        gridView = findViewById(R.id.profile_grid);
        profile_userimage = findViewById(R.id.profile_userimage);

        Intent intent = getIntent();

        //툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // 아이디 세팅
        login_id = LoginSharedPreference.getLoginId(ProfileActivity.this);
        user_id = intent.getStringExtra("userId");

        if(!login_id.equals(user_id) ) { //남의 프로필 보기
            user_id = intent.getStringExtra("userId");
            //actionBar.setTitle(LoginSharedPreference.getLoginId(this));
            actionBar.setTitle(intent.getStringExtra("userId"));
            Status = OTHER;
        }
        else { //내 프로필
            actionBar.setTitle(login_id);
            Status = MY;
        }

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        getProfileData();

        profile_follows = findViewById(R.id.profile_follows);

        profile_follows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProfileActivity.this, FollowActivity.class);

                // intro, picture 전달
                intent.putExtra("userId", user_id );
                intent.putExtra("intro", user_data.getIntro());
                intent.putExtra("picture", user_data.getImg_profile());

               startActivity(intent);
            }
        });


        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(ProfileActivity.this, PostActivity.class);

                // 게시글 id 전달
                int postId = post_data.get(position).getId();
                intent.putExtra("post_id", postId);

                startActivity(intent);
                Log.d("TAG", position + "is Clicked");      // Can not getting this method.
            }
        });

        // 팔로우 버튼 Click Listener
        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowData data = new FollowData(login_id, user_id);
                serviceApi.Follow(data).enqueue(new Callback<CodeResponse>() {
                    @Override
                    public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                        CodeResponse result = response.body();
                        int resultCode = result.getCode();
                        if(resultCode == statusCode.RESULT_OK){
                            follow_btn.setVisibility(View.INVISIBLE);
                            unfollow_btn.setVisibility(View.VISIBLE);
                            unfollow_btn.setEnabled(true);
                            int follower_cnt = Integer.parseInt(follower_count_tv.getText().toString());
                            follower_cnt++;
                            follower_count_tv.setText(follower_cnt+"");
                        }
                        else if(resultCode == statusCode.RESULT_CLIENT_ERR){
                            new AlertDialog.Builder(ProfileActivity.this)
                                    .setTitle("경고")
                                    .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .show();
                        }
                        else {
                            Toast.makeText(ProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CodeResponse> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        Log.e("팔로우 하기 에러", t.getMessage());
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
            }
        });

        // 언팔로우 버튼 Click Listener
        unfollow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowData data = new FollowData(login_id, user_id);
                serviceApi.UnFollow(data).enqueue(new Callback<CodeResponse>() {
                    @Override
                    public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                        CodeResponse result = response.body();
                        int resultCode = result.getCode();
                        if(resultCode == StatusCode.RESULT_OK){
                            unfollow_btn.setVisibility(View.INVISIBLE);
                            follow_btn.setVisibility(View.VISIBLE);
                            follow_btn.setEnabled(true);
                            int follower_cnt = Integer.parseInt(follower_count_tv.getText().toString());
                            follower_cnt--;
                            follower_count_tv.setText(follower_cnt+"");
                        }
                        else if(resultCode == statusCode.RESULT_SERVER_ERR){
                            new AlertDialog.Builder(ProfileActivity.this)
                                    .setTitle("경고")
                                    .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .show();
                        }
                        else {
                            Toast.makeText(ProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CodeResponse> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        Log.e("언팔로우 하기 에러", t.getMessage());
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
            }
        });
    }



    // server에서 data전달
    public void getProfileData(){
        UserData data = new UserData(login_id, Status);
        if(Status == OTHER) // 타인의 프로필일 경우
            data.SetUserId(user_id);

        serviceApi.Profile(data).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse result = response.body();
                int resultCode = result.getCode();

                if(resultCode == statusCode.RESULT_OK){
                    setProfileData(result);
                }
                else if(resultCode == statusCode.RESULT_CLIENT_ERR){
                    new AlertDialog.Builder(ProfileActivity.this)
                            .setTitle("경고")
                            .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 에러 발생 시 새로고침
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                Log.e("프로필 데이터 불러오기 에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
    }

    public void setProfileData(ProfileResponse obj){
        post_data = obj.getPostInfo();
        user_data = obj.getProfileInfo();


        follow_count_tv.setText(obj.getFollowCnt()+"");
        follower_count_tv.setText(obj.getFollowerCnt()+"");

        intro_tv.setText(user_data.getIntro());
        sns_tv.setText(user_data.getSns());


        String img_addr = RetrofitClient.getBaseUrl()+ user_data.getImg_profile();
        Log.d("profile", img_addr);
        Glide.with(this).load( img_addr).into(profile_userimage);
        feed_count_tv.setText(post_data.size()+"");

        adapter = new PostImageAdapter(this, R.layout.post_image_item, post_data);
        gridView.setAdapter(adapter);


        if(Status == MY) { // 내 프로필
            follow_btn.setEnabled(false);
            follow_btn.setVisibility(View.INVISIBLE);
            pointview.setVisibility(View.VISIBLE);
            points_tv.setText(user_data.getPoint()+ "p");
        }
        else if( Status == OTHER ){ // 타인 프로필
            int isFollowing = user_data.getFlag();

            if(isFollowing == 0) { // 팔로우 x
                follow_btn.setEnabled(true);
                follow_btn.setVisibility(View.VISIBLE);
            }
            else{ //팔로우중
                follow_btn.setEnabled(false);
                follow_btn.setVisibility(View.INVISIBLE);
                unfollow_btn.setEnabled(true);
                unfollow_btn.setVisibility(View.VISIBLE);
            }

            pointview.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                finish();
                break;

            case R.id.profile_edit:
                Intent intent1 = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent1);
                finish();
                //startActivityForResult(intent1, REQUEST_CODE);
                break;

            case R.id.profile_keep:
                Intent intent2 = new Intent(ProfileActivity.this , KeepActivity.class);
                startActivity(intent2);
                break;

            case R.id.profile_logout:
                // LogoutAction
                LoginSharedPreference.clearLogin(ProfileActivity.this);
                Intent intent3 = new Intent(ProfileActivity.this, LoginActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);

                break;
            case R.id.profile_setting:
                Intent intent4 = new Intent(ProfileActivity.this , SettingActivity.class);
                startActivity(intent4);
                break;

            case R.id.profile_account_edit:
                Intent intent5 = new Intent(ProfileActivity.this , EditAccountActivity.class);

                startActivity(intent5);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == REQUEST_CODE){
//            if(resultCode != Activity.RESULT_OK){
//                return;
//            }
//            String new_id = data.getStringExtra("userId");
//            Intent intent = getIntent();
//            intent.putExtra("userId", new_id);
//            finish();
//            startActivity(intent);
//        }
//    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}
