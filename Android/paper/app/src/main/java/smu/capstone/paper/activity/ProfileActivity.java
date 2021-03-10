package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.item.PostItem;


public class ProfileActivity extends AppCompatActivity {
    public int Status;

    final int MY = 1;
    final int FOLLOWING = 2;
    final int UNFOLLOWING = 3;

    private GridView gridView;
    private PostImageAdapter adapter;
    private TextView feed_count_tv, follow_count_tv, follower_count_tv, points_tv, intro_tv, sns_tv;
    private Button follow_btn;
    private LinearLayout profile_follows;
    private LinearLayout pointview;
    private JSONObject post_item,profile_item;
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

        //툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("User ID");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정




        setProfileData();
        setPostData();

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
                startActivity(intent);
            }
        });
    }



    // server에서 data전달
    public JSONObject getProfileData(){

        profile_item = new JSONObject();

        // 임시 데이터 저장
        try{
            JSONObject obj = new JSONObject();
            obj.put("follow_count", 100);
            obj.put("follower_count", 291);
            obj.put("point", 4002);
            obj.put("sns", "https://www.google.com");
            obj.put("intro", "Good to see you Buddy!");
            obj.put("picture", R.drawable.ic_baseline_emoji_emotions_24); //추후에 url 세팅으로변경

            return obj;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return profile_item;
    }

    public void setProfileData(){

        JSONObject data = getProfileData();
        try {
            follow_count_tv.setText(data.getInt("follow_count") +"");
            follower_count_tv.setText(data.getInt("follower_count" )+"");
            points_tv.setText(data.getInt("point") + "p");
            intro_tv.setText(data.getString("intro"));
            sns_tv.setText(data.getString("sns"));
            Glide.with(this).load(data.getInt("picture")).into(profile_userimage); // 게시물 사진

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject getPostData(){
        post_item = new JSONObject();
        JSONArray arr= new JSONArray();


        // 임시 데이터 저장
        try{
            JSONObject obj = new JSONObject();
            obj.put("postId", "111");
            obj.put("postImage", R.drawable.ic_favorite);
            arr.put(obj);

            JSONObject obj2 = new JSONObject();
            obj2.put("postId", "123");
            obj2.put("postImage", R.drawable.ic_favorite_border);
            arr.put(obj2);

            JSONObject obj3 = new JSONObject();
            obj3.put("postId", "144");
            obj3.put("postImage", R.drawable.ic_favorite);
            arr.put(obj3);

            post_item.put("data", arr);

        }catch (JSONException e){ e.printStackTrace(); }

        return post_item;
    }

    // 서버에서 데이터가져온후 어뎁터에 세팅
    public void setPostData(){
        JSONObject data = getPostData();
        try {
            JSONArray arr = data.getJSONArray("data");
            feed_count_tv.setText(arr.length()+"");
            adapter = new PostImageAdapter(this,  R.layout.post_image_item , data ) ;
            gridView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
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
                LoginSharedPreference.clearUserName(ProfileActivity.this);
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