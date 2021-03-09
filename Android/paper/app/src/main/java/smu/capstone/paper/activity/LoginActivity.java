package smu.capstone.paper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;

public class LoginActivity extends AppCompatActivity {

    Button login_go_join;
    Button login_go_find;
    Button login_btn;
    EditText login_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_username = findViewById(R.id.login_username);
        login_btn = findViewById(R.id.login_btn);

        login_go_join = findViewById(R.id.login_go_join);
        login_go_find = findViewById(R.id.login_go_find);

        login_go_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        login_go_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindAccountActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(true){ //로그인 성공시

                    LoginSharedPreference.setUserName(LoginActivity.this, "administer");

                    //로그인 기록 저장
                    //LoginSharedPreference.setUserName(LoginActivity.this, login_username.getText().toString());
                }
                else{ //로그인 실패

                    // 로그인 실패 알림
                }

                //일단 바로 홈화면으로 전환
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);


            }
        });
        // 텍스트 입력시 로그인 버튼 활성화
        login_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    login_btn.setClickable(true);
                    login_btn.setBackgroundColor(0x9A93C8B4);
                }
            }
        });
    }
}
