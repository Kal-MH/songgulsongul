package com.smu.songgulsongul.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.data.LoginData;
import com.smu.songgulsongul.responseData.LoginResponse;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class LoginActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    StatusCode statusCode;
    Button login_go_join;
    Button login_go_find;
    Button login_btn;
    EditText login_username, login_pw;


    //debug
    Button devLoginPassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_username = findViewById(R.id.login_username);
        login_pw = findViewById(R.id.login_pw);
        login_btn = findViewById(R.id.login_btn);

        login_go_join = findViewById(R.id.login_go_join);
        login_go_find = findViewById(R.id.login_go_find);

        //debug
        devLoginPassButton = findViewById(R.id.devButton);

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

                final String login_id = login_username.getText().toString();
                String passsword = login_pw.getText().toString();
                login_id.trim();
                passsword.trim();


                // 입력한 아이디가 공백값일 경우 --> 서버 통신 x
                if (login_id.getBytes().length <= 0) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("경고")
                            .setMessage("아이디를 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                }

                // 입력한 비밀번호가 공백값일 경우 --> 서버 통신 x
                else if (passsword.getBytes().length <= 0) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("경고")
                            .setMessage("비밀번호를 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                // login_id, password 로 서버와 통신
                else {
                    LoginData data = new LoginData(login_id, passsword);
                    serviceApi.Login(data).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse result = response.body();
                            int resultCode = result.getCode();

                            // 로그인 성공시 --> 로그인 기록 저장, home으로 전환
                            if (resultCode == statusCode.RESULT_OK ) { // 로그인성공, 오늘의 첫로그인

                                int user_id = result.getId();
                                LoginSharedPreference.setLogin(LoginActivity.this, user_id, login_id);
                                setToken();
                                Toast.makeText(LoginActivity.this, "출석체크 되었습니다!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else if( resultCode == statusCode.RESULT_NO){ // 로그인 성공, 오늘의 첫로그인 아님
                                int user_id = result.getId();
                                LoginSharedPreference.setLogin(LoginActivity.this, user_id, login_id);
                                setToken();
                                Toast.makeText(LoginActivity.this, "반갑습니다!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else if (resultCode == statusCode.RESULT_CLIENT_ERR) {
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setMessage("아이디, 또는 패스워드가 잘못 입력되었습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                                login_username.setText(null);
                                login_pw.setText(null);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                            Log.e("로그인 에러", t.getMessage());
                            t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                        }
                    });
                }
            }
        });


        //debug
        devLoginPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String login_id = login_username.getText().toString();
                String passsword = login_pw.getText().toString();
                login_id.trim();
                passsword.trim();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
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

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    login_btn.setClickable(true);
                    login_btn.setBackgroundResource(R.color.purpleGrayDark);
                }
                else{
                    login_btn.setClickable(false);
                    login_btn.setBackgroundResource(R.color.purpleGray);
                }
            }
        });
    }
}
