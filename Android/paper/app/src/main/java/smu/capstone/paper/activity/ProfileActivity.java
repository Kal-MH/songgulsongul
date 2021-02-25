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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import smu.capstone.paper.R;


public class ProfileActivity extends AppCompatActivity {

    private LinearLayout profile_follows;
    private ImageButton profile_menu_btn;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
      //  actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setTitle("User ID");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정




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
                Log.d("TAG", "뒤로,,,");
                finish();
                break;

            case R.id.profile_edit :
                // EditProfileActivity로 이동
                break;

            case R.id.profile_keep:
                //KeepActivity로 이동
                break;

            case R.id.profile_logout:
                // LogoutAction
                break;
            case R.id.profile_setting:
                Intent intent = new Intent(ProfileActivity.this , SettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}