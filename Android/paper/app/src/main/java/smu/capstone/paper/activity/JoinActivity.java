package smu.capstone.paper.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.R;
import smu.capstone.paper.data.JoinData;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;

public  class JoinActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    TextView join_timer, join_id_text, join_pw_correct_text;
    CountDownTimer countDownTimer;
    Button join_send_btn, join_check_key, join_check_id, join_btn;
    RadioGroup join_sns_radio;
    EditText join_sns_text, join_, join_email_text,  join_pw_text, join_pw_check_text;

    String auth;
    final int MILLISINFUTURE = 180 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000;
    int id_check_flag = 0; // 아이디 중복 확인 여부 체크
    int email_check_flag = 0; // 이메일 중복 확인 여부 체크
    int password_check_flag = 0; // 비밀번호 체크

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_send_btn = (Button)findViewById(R.id.join_send_btn);
        join_check_key = (Button)findViewById(R.id.join_check_key);
        join_timer = findViewById(R.id.join_timer);
        join_sns_radio = (RadioGroup)findViewById(R.id.join_sns_radio);
        join_sns_text = (EditText)findViewById(R.id.join_sns_text);
        join_check_id = (Button)findViewById(R.id.join_check_id);
        join_id_text = (TextView)findViewById(R.id.join_id_text);
        join_ = (EditText) findViewById(R.id.join_);
        join_email_text = (EditText)findViewById(R.id.join_email_text);
        join_pw_text = (EditText)findViewById(R.id.join_pw_text);
        join_pw_check_text = (EditText)findViewById(R.id.join_pw_check_text);
        join_pw_correct_text = (TextView)findViewById(R.id.join_pw_correct_text);
        join_btn = (Button)findViewById(R.id.join_btn);

        join_check_key.setEnabled(false);
        join_timer.setVisibility(View.INVISIBLE); // 인증 제한시간 숨기기
        join_pw_correct_text.setVisibility(View.INVISIBLE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.join_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setTitle("Join");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        // 아이디 중복 확인 버튼
        join_check_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login_id = join_id_text.getText().toString();

                // 입력한 아이디가 공백값일 경우 --> 서버 통신 x
                login_id = login_id.trim();
                if(login_id.getBytes().length <= 0){
                    id_check_flag = 0;
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle("경고")
                            .setMessage("아이디를 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                else {
                    // login_id로 server와 통신
                    serviceApi.IdCheck(login_id).enqueue(new Callback<JSONObject>() {

                        // 통신 성공시 호출
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            JSONObject result = response.body();
                            try {
                                int resultCode = result.getInt("code");

                                if (resultCode == 200) {
                                    id_check_flag = 1;
                                    new AlertDialog.Builder(JoinActivity.this)
                                            .setMessage("사용할 수 있는 아이디입니다.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .show();
                                }

                                else {
                                    id_check_flag = 0;
                                    new AlertDialog.Builder(JoinActivity.this)
                                            .setTitle("경고")
                                            .setMessage("이미 사용중인 아이디입니다."+"\n"+"다시 입력해 주세요.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .show();
                                    join_id_text.setText(null);
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        // 통신 실패시 호출
                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            id_check_flag = 0;
                            Toast.makeText(JoinActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                            Log.e("아이디 중복확인 에러", t.getMessage());
                            t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                        }
                    });
                }
            }
        });

        // 비밀번호 일치 확인
        join_pw_check_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(join_pw_check_text.getText().toString())) {
                    join_pw_correct_text.setText("비밀번호가 일치합니다.");
                    join_pw_correct_text.setVisibility(View.VISIBLE);
                    password_check_flag = 1;
                }
                else {
                    join_pw_correct_text.setText("비밀번호가 일치하지 않습니다.");
                    join_pw_correct_text.setVisibility(View.VISIBLE);
                    password_check_flag = 0;
                }
            }
        });

        // 이메일 인증 버튼 눌렀을 시 발생
        join_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern email_pattern = Pattern.compile("^[a-zA-X0-9]@[a-zA-Z0-9].[a-zA-Z0-9]");
                String email = join_email_text.getText().toString();
                email = email.trim();

                // 입력한 이메일이 공백값일 경우 --> 서버 통신 x
                if(email.getBytes().length <= 0){
                    email_check_flag = 0;
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle("경고")
                            .setMessage("이메일을 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                // 입력한 이메일이 형식에서 벗어날 경우 --> 서버 통신x
                else if(!email_pattern.matcher(email).matches()){
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle("경고")
                            .setMessage("올바른 이메일 형식이 아닙니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                else {
                    // 입력한 email로 server통신
                    serviceApi.EmailAuth(email).enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            JSONObject result = response.body();
                            try {
                                auth = result.getString("authNumber");
                                int resultCode = result.getInt("code");

                                if(resultCode == 200) {
                                    join_check_key.setEnabled(true); // 인증번호 확인 버튼 활성화
                                    join_timer.setVisibility(View.VISIBLE); // 인증 제한시간 표시
                                    countDownTimer(); // 타이머 작동
                                }
                                else{
                                    Toast.makeText(JoinActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            email_check_flag = 0;
                            Toast.makeText(JoinActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                            Log.e("이메일 인증 에러", t.getMessage());
                            t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                        }
                    });
                }
            }
        });

        // 인증번호 입력 -> 확인 버튼 눌렀을 시 발생
        join_check_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputAuth = join_.getText().toString();
                inputAuth = inputAuth.trim();

                // 인증번호 입력이 공백값일 경우
                if(inputAuth.getBytes().length <= 0){
                    email_check_flag = 0;
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle("경고")
                            .setMessage("인증번호를 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
                else {
                    countDownTimer.cancel(); // 타이머 종료

                    if(inputAuth.equals(auth)){
                        email_check_flag = 1;
                        new AlertDialog.Builder(JoinActivity.this)
                                .setMessage("인증되었습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                    else{
                        email_check_flag = 0;
                        new AlertDialog.Builder(JoinActivity.this)
                                .setMessage("인증실패!"+"\n"+"다시 시도해주세요.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                        join_email_text.setText(null);
                        join_.setText(null);
                    }
                }
            }
        });

        // sns 계정 입력
        join_sns_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // sns 무 선택 -> 입력창 비활성화
                if(checkedId == R.id.join_sns_n) {
                    join_sns_text.setClickable(false);
                    join_sns_text.setFocusable(false);
                    join_sns_text.setText(null);
                }
                // sns 유 선택 -> 입력창 활성화
                else{
                    join_sns_text.setFocusableInTouchMode(true);
                    join_sns_text.setFocusable(true);
                }
            }
        });

        // 회원가입 버튼
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = join_email_text.getText().toString();
                String password = join_pw_text.getText().toString();
                String login_id = join_id_text.getText().toString();
                String sns_url = join_sns_text.getText().toString();

                // 입력값 공백 제거
                email = email.trim();
                password = password.trim();
                login_id = login_id.trim();
                sns_url = sns_url.trim();

                int email_num = email.getBytes().length;
                int password_num = password.getBytes().length;
                int login_id_num = login_id.getBytes().length;
                int sns_url_num = sns_url.getBytes().length;

                // 미입력한 값이 있을 경우 --> 서버 통신x
                if(email_num <= 0 || password_num <= 0 || login_id_num <= 0){
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle("경고")
                            .setMessage("항목을 모두 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                // 아이디 중복확인 완료x --> 서버 통신x
                else if(id_check_flag == 0){
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle("경고")
                            .setMessage("아이디 중복확인을 완료해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                // 이메일 인증 완료x --> 서버 통신x
                else if(email_check_flag == 0){
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle("경고")
                            .setMessage("이메일 인증을 완료해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                // 비밀번호 일치 여부 확인x --> 서버 통신x
                else if(password_check_flag == 0){
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle("경고")
                            .setMessage("비밀번호 확인을 완료해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                else {
                    // joindata로 server와 통신
                    JoinData data = new JoinData(email, password, login_id, sns_url);
                    serviceApi.Join(data).enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            JSONObject result = response.body();
                            try {
                                int resultCode = result.getInt("code");
                                if (resultCode == 200) {
                                    new AlertDialog.Builder(JoinActivity.this)
                                            .setMessage("회원가입 완료!")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // 회원가입 성공시 로그인 화면으로 전환
                                                    Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();
                                } else {
                                    new AlertDialog.Builder(JoinActivity.this)
                                            .setMessage("회원가입 실패" + "\n" + "다시 시도해주세요.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // 회원가입 실패시 화면 새로고침 --> 처음부터 재시도
                                                    Intent intent = getIntent();
                                                    finish();
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            Toast.makeText(JoinActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                            Log.e("회원가입 에러", t.getMessage());
                            t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                        }
                    });
                }
            }
        });
    }

    public void countDownTimer(){

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL ) {
            @Override
            public void onTick(long millisUntilFinished) {
                long endCount = millisUntilFinished / 1000;

                if((endCount - ((endCount / 60) * 60)) >= 10){
                    join_timer.setText((endCount / 60) + " : " + (endCount - ((endCount / 60) * 60)));
                }
                else // 10초 이하일 때 0붙여 출력
                    join_timer.setText((endCount / 60) + " : 0" + (endCount - ((endCount / 60) *60)));
            }

            @Override
            public void onFinish() { // 카운트 종료 시 일어날 일
                join_check_key.setEnabled(false); // 인증번호 확인 버튼 비활성화
            }
        }.start();
    }

    public JSONObject getAuthData(String email){
        final JSONObject item = new JSONObject();

        //임시 데이터 저장
        try{
            item.put("authNumber", "123456");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}