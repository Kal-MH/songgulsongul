package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

import java.util.ArrayList;

import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.item.PostItem;


public class ProfileActivity extends AppCompatActivity {
    public int Status = 3;
    TextView feed_count, follow_count, follower_count, points, intro, snsurl;

    ArrayList<PostItem> items = new ArrayList<PostItem>();
    private LinearLayout profile_follows;
    private ImageButton profile_menu_btn;
    private ImageView post_image_item;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        feed_count = findViewById(R.id.profile_feed_cnt);
        follow_count = findViewById(R.id.profile_follow_cnt);
        follower_count = findViewById(R.id.profile_follower_cnt);
        points = findViewById(R.id.profile_point);
        intro = findViewById(R.id.profile_intro);
        snsurl = findViewById(R.id.profile_snsurl);

        Button follow_btn = findViewById(R.id.profile_follow_btn);
        LinearLayout pointview = findViewById(R.id.profile_pointview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
      //  actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setTitle("User ID");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        //Status에 떄라 버튼과 포인트 visibility와 enable 설정
        switch(Status){
            //본인의 계정 프로필
            case 1:
                follow_btn.setEnabled(false);
                follow_btn.setVisibility(View.INVISIBLE);
                pointview.setVisibility(View.VISIBLE);
                break;
            //팔로우 한 타인의 프로필
            case 2:
                follow_btn.setEnabled(false);
                follow_btn.setVisibility(View.VISIBLE);
                pointview.setVisibility(View.INVISIBLE);
                break;
            //팔로우 하지 않은 타인의 프로필
            case 3:
                follow_btn.setEnabled(true);
                follow_btn.setVisibility(View.VISIBLE);
                pointview.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        follow_count.setText("12");
        follower_count.setText("34");
        points.setText("100pt");
        intro.setText("안녕");
        snsurl.setText("https://www.google.com");

        // view에서 id 찾아야함
        GridView gridView = findViewById(R.id.profile_grid);
        //아이템 추가
        items.add(new PostItem(R.drawable.sampleimg));
        items.add(new PostItem(R.drawable.test));
        items.add(new PostItem(R.drawable.ic_baseline_emoji_emotions_24));
        items.add(new PostItem(R.drawable.test));
        items.add(new PostItem(R.drawable.sampleimg));
        items.add(new PostItem(R.drawable.ic_favorite));
        items.add(new PostItem(R.drawable.sampleimg));

        feed_count.setText("" + items.size());

        // 어뎁터 적용
        PostImageAdapter adapter = new PostImageAdapter(this,  R.layout.post_image_item , items ) ;
        gridView.setAdapter(adapter);

        profile_follows = findViewById(R.id.profile_follows);

        profile_follows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProfileActivity.this, FollowActivity.class);
                startActivity(intent);
            }
        });
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