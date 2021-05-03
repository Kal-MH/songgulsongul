package smu.capstone.paper.activity;

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
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.data.CodeResponse;
import smu.capstone.paper.data.LoginData;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;

public class LoginActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    Button login_go_join;
    Button login_go_find;
    Button login_btn;
    EditText login_username, login_pw;

    final int RESULT_OK = 200;
    final int RESULT_CLIENT_ERR= 204;
    final int RESULT_SERVER_ERR = 500;

    boolean test = true;

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



                if(test){ // 현재 boolean test = true 이기 때문에 이 코드만 실행후 종료
                    if (login_username.getText().toString().length() == 0)
                        LoginSharedPreference.setLoginId(LoginActivity.this, "administer");
                    else
                        LoginSharedPreference.setLoginId(LoginActivity.this, login_id);


                    //로그인 기록 저장
                    LoginSharedPreference.setLoginId(LoginActivity.this,login_id);

                    //   일단 바로 홈화면으로 전환
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                    return ;
                }



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

                // login_id, password로 서버와 통신
                else {
                    LoginData data = new LoginData(login_id, passsword);
                    serviceApi.Login(data).enqueue(new Callback<CodeResponse>() {
                        @Override
                        public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                            CodeResponse result = response.body();
                            int resultCode = result.getCode();

                            // 로그인 성공시 --> 로그인 기록 저장, home으로 전환
                            if (resultCode == RESULT_OK) {
                                LoginSharedPreference.setLoginId(LoginActivity.this, login_id);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (resultCode == RESULT_CLIENT_ERR) {
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
                            } else {
                                Toast.makeText(LoginActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CodeResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
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
