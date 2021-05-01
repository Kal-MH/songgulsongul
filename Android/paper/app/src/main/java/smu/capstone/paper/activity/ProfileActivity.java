package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.data.ProfileData;
import smu.capstone.paper.data.UserData;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;


public class ProfileActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    final int RESULT_OK = 200;
    final int RESULT_CLIENT_ERR= 204;
    final int RESULT_SERVER_ERR = 500;

    final int MY = 1;
    final int FOLLOWING = 2;
    final int UNFOLLOWING = 3;
    public int Status = 1;
    String login_id = "test1234";
    String user_id = "aaa";
    //String login_id = LoginSharedPreference.getLoginId(this);

    private GridView gridView;
    private PostImageAdapter adapter;
    private TextView feed_count_tv, follow_count_tv, follower_count_tv, points_tv, intro_tv, sns_tv;
    private Button follow_btn;
    private LinearLayout profile_follows;
    private LinearLayout pointview;
    private JSONObject post_item;
    private JsonObject obj, profile_item;
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
        pointview = findViewById(R.id.profile_pointview);
        gridView = findViewById(R.id.profile_grid);
        profile_userimage = findViewById(R.id.profile_userimage);

        Intent intent = getIntent();

        //툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // 아이디 세팅
        /*if(!login_id.equals(intent.getStringExtra("userid") ) && intent.getStringExtra("userid") != null) {
            user_id = intent.getStringExtra("userid");
            //actionBar.setTitle(LoginSharedPreference.getLoginId(this));
            actionBar.setTitle(intent.getStringExtra("userid"));
        }
        else {
            actionBar.setTitle(login_id);
            Status = MY;
        }*/
        actionBar.setTitle(LoginSharedPreference.getLoginId(this));

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        getProfileData();
        //setProfileData(profile_item);

        //Status에 떄라 버튼과 포인트 visibility와 enable 설정
        switch(Status){
            //본인의 계정 프로필
            case MY:
                follow_btn.setEnabled(false);
                follow_btn.setVisibility(View.INVISIBLE);
                pointview.setVisibility(View.VISIBLE);
                break;
            //팔로우 한 타인의 프로필
            case FOLLOWING :
                follow_btn.setEnabled(false);
                follow_btn.setVisibility(View.VISIBLE);
                pointview.setVisibility(View.INVISIBLE);
                break;
            //팔로우 하지 않은 타인의 프로필
            case UNFOLLOWING:
                follow_btn.setEnabled(true);
                follow_btn.setVisibility(View.VISIBLE);
                pointview.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }


        profile_follows = findViewById(R.id.profile_follows);

        profile_follows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProfileActivity.this, FollowActivity.class);

                // intro, picture 전달
                JsonArray profile_info = profile_item.getAsJsonArray("profileInfo");
                intent.putExtra("intro", profile_info.get(0).getAsJsonObject().get("intro").getAsString());
                intent.putExtra("picture", profile_info.get(0).getAsJsonObject().get("profile_image").getAsString());


               startActivity(intent);
            }
        });


        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(ProfileActivity.this, PostActivity.class);

                // 게시글 id 전달
                int postId = profile_item.getAsJsonArray("postInfo").get(position).getAsJsonObject().get("postId").getAsInt();
                intent.putExtra("postId", postId);

                startActivity(intent);
                Log.d("TAG", position + "is Clicked");      // Can not getting this method.
            }
        });
    }



    // server에서 data전달
    public void getProfileData(){
        UserData data = new UserData(login_id, Status);
        if(Status != 1) // 타인의 프로필일 경우
            data.SetUserId(user_id);

        serviceApi.Profile(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();
                int resultCode = result.get("code").getAsInt();

                if(resultCode == RESULT_OK){
                    profile_item = result;
                    setProfileData(result);
                }
                else if(resultCode == RESULT_CLIENT_ERR){
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
                else{
                    Toast.makeText(ProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                Log.e("프로필 데이터 불러오기 에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
    }

    public void setProfileData(JsonObject obj){
        JsonObject post_data = new JsonObject();
        JsonArray arr = obj.getAsJsonArray("postInfo");
        post_data.add("data", arr);

        follow_count_tv.setText(obj.get("followCnt").getAsInt() +"");
        follower_count_tv.setText(obj.get("followerCnt" ).getAsInt()+"");

        if(Status == MY)
            points_tv.setText(obj.getAsJsonArray("profileInfo").get(0).getAsJsonObject().get("point").getAsInt() + "p");

        intro_tv.setText(obj.getAsJsonArray("profileInfo").get(0).getAsJsonObject().get("intro").getAsString());
        sns_tv.setText(obj.getAsJsonArray("profileInfo").get(0).getAsJsonObject().get("sns").getAsString());
        Glide.with(this).load(obj.getAsJsonArray("profileInfo").get(0).getAsJsonObject().get("profile_image").getAsString()).into(profile_userimage);

        feed_count_tv.setText(obj.getAsJsonArray("postInfo").size()+"");
        adapter = new PostImageAdapter(this,  R.layout.post_image_item , post_data ) ;
        gridView.setAdapter(adapter);
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

            case R.id.profile_edit :
                Intent intent1 = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent1);
                break;

            case R.id.profile_keep:
                Intent intent2 = new Intent(ProfileActivity.this , KeepActivity.class);
                startActivity(intent2);
                break;

            case R.id.profile_logout:
                // LogoutAction
                LoginSharedPreference.clearLoginId(ProfileActivity.this);
                Intent intent3 = new Intent(ProfileActivity.this, LoginActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);

                break;
            case R.id.profile_setting:
                Intent intent4 = new Intent(ProfileActivity.this , SettingActivity.class);
                startActivity(intent4);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}