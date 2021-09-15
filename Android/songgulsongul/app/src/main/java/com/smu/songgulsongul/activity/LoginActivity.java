package com.smu.songgulsongul.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.data.user.LoginData;
import com.smu.songgulsongul.data.user.TokenData;
import com.smu.songgulsongul.data.CodeResponse;
import com.smu.songgulsongul.data.user.response.LoginResponse;
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

    int BackColor = Color.parseColor("#BFB1D8");
    int FontColor = Color.parseColor("#000000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_username = findViewById(R.id.login_username);
        login_pw = findViewById(R.id.login_pw);
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

                final String login_id = login_username.getText().toString();
                String passsword = login_pw.getText().toString();
                login_id.trim();
                passsword.trim();

                //opencv Debug Pass code! need to remove
                //Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                //startActivity(intent);
                //finish();


                // 입력한 아이디가 공백값일 경우 --> 서버 통신 x
                if (login_id.getBytes().length <= 0) {
                    View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ImageView icon = dialogView.findViewById(R.id.warning);

                    TextView txt = dialogView.findViewById(R.id.txtText);
                    txt.setText("아이디를 입력해주세요.");
                    Button ok_btn = dialogView.findViewById(R.id.okBtn);
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                    cancel_btn.setVisibility(View.GONE);


                }

                // 입력한 비밀번호가 공백값일 경우 --> 서버 통신 x
                else if (passsword.getBytes().length <= 0) {
                    View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ImageView icon = dialogView.findViewById(R.id.warning);

                    TextView txt = dialogView.findViewById(R.id.txtText);
                    txt.setText("비밀번호를 입력해주세요.");
                    Button ok_btn = dialogView.findViewById(R.id.okBtn);
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                    cancel_btn.setVisibility(View.GONE);
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
                            if (resultCode == StatusCode.RESULT_OK) { // 로그인성공, 오늘의 첫로그인

                                int user_id = result.getId();
                                LoginSharedPreference.setLogin(LoginActivity.this, user_id, login_id);
                                setToken();
                                Toasty.custom(LoginActivity.this, "출석체크 되었습니다!", null, BackColor, FontColor, 2000, false, true).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (resultCode == StatusCode.RESULT_NO) { // 로그인 성공, 오늘의 첫로그인 아님
                                int user_id = result.getId();
                                LoginSharedPreference.setLogin(LoginActivity.this, user_id, login_id);
                                setToken();
                                Toasty.custom(LoginActivity.this, "반갑습니다!", null, BackColor, FontColor, 2000, false, true).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (resultCode == StatusCode.RESULT_CLIENT_ERR) {
                                View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setView(dialogView);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                ImageView icon = dialogView.findViewById(R.id.warning);
                                icon.setVisibility(View.GONE);

                                TextView txt = dialogView.findViewById(R.id.txtText);
                                txt.setText("아이디, 또는 패스워드가 잘못 입력되었습니다.");
                                Button ok_btn = dialogView.findViewById(R.id.okBtn);
                                ok_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                                Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                                cancel_btn.setVisibility(View.GONE);

                                login_username.setText(null);
                                login_pw.setText(null);
                            } else {
                                Toasty.normal(LoginActivity.this, "서버와의 통신이 불안정합니다").show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toasty.normal(LoginActivity.this, "서버와의 통신이 불안정합니다").show();
                            Log.e("로그인 에러", t.getMessage());
                            t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                        }
                    });

                }
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
                if (s.length() > 0) {
                    login_btn.setClickable(true);
                    login_btn.setBackgroundResource(R.color.purpleGrayDark);
                } else {
                    login_btn.setClickable(false);
                    login_btn.setBackgroundResource(R.color.purpleGray);
                }
            }
        });
    }

    public void setToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    LoginSharedPreference.setToken(LoginActivity.this, token);

                    TokenData tokenData = new TokenData(LoginSharedPreference.getUserId(LoginActivity.this), token);
                    serviceApi.setToken(tokenData)
                            .enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {

                                    Toasty.custom(LoginActivity.this, "기기를 등록했습니다", null, BackColor, FontColor, 2000, false, true).show();

                                }

                                @Override
                                public void onFailure(Call<CodeResponse> call, Throwable t) {

                                    Toasty.normal(LoginActivity.this, "서버와의 통신이 불안정합니다").show();
                                    Log.e("Token", "등록 실패");
                                }

                            });


                }
            }
        });
    }


}
