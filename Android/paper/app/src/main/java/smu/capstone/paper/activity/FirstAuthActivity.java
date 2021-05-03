package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        if(LoginSharedPreference.getLoginId(FirstAuthActivity.this).length() == 0) {
            // 로그인 기록 저장 x --> 로그인 화면으로
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
        }
        else { // 로그인 저장되어있음 -> 서버통신없이 자동 로그인
            intent = new Intent(FirstAuthActivity.this, HomeActivity.class);
            intent.putExtra("STD_NUM", LoginSharedPreference.getLoginId(this).toString());
        }
        startActivity(intent);
        this.finish();
    }
}