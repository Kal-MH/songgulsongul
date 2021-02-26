package smu.capstone.paper.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;

import smu.capstone.paper.R;

public class EditProfileActivity extends AppCompatActivity {

    RadioGroup sns_radio;
    EditText profile_new_sns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("프로필 수정");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        profile_new_sns = findViewById(R.id.profile_new_sns);
        sns_radio = findViewById(R.id.profile_edit_sns_radio);
        sns_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // sns 무 선택 -> 입력창 비활성화

                if(checkedId == R.id.profile_sns_radio_no) {
                    profile_new_sns.setClickable(false);
                    profile_new_sns.setFocusable(false);
                    profile_new_sns.setHint("sns 계정 없음");
                    profile_new_sns.setText("");
                }
                // sns 유 선택 -> 입력창 활성화
                else{
                    profile_new_sns.setFocusableInTouchMode(true);
                    profile_new_sns.setFocusable(true);
                    profile_new_sns.setHint("sns 계정을 입력하세요");
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.toolbar_save:
                return true;

            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                finish();
                break;
        }
        return  true;
    }




}