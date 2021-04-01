package smu.capstone.paper.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.R;

public  class JoinActivity extends AppCompatActivity {

    TextView join_timer, join_id_text;
    CountDownTimer countDownTimer;
    Button join_send_btn, join_check_key, join_check_id;
    RadioGroup join_sns_radio;
    EditText join_sns_text, join_, join_email_text;

    String auth;
    final int MILLISINFUTURE = 180 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000;

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

        join_check_key.setEnabled(false);
        join_timer.setVisibility(View.INVISIBLE); // 인증 제한시간 숨기기


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


                    //if resultCode == 200
                    new AlertDialog.Builder(JoinActivity.this)
                            .setMessage("사용할 수 있는 아이디입니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                    //else
                /*new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("경고")
                        .setMessage("이미 사용중인 아이디입니다."+"\n"+"다시 입력해 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();*/
                }
            }
        });


        // 이메일 인증 버튼 눌렀을 시 발생
        join_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = join_email_text.getText().toString();
                email = email.trim();

                // 입력한 이메일이 공백값일 경우 --> 서버 통신 x
                if(email.getBytes().length <= 0){
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

                else {
                    // 입력한 email로 server통신
                    try {
                        auth = getAuthData().getString("authNumber");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    join_check_key.setEnabled(true); // 인증번호 확인 버튼 활성화
                    join_timer.setVisibility(View.VISIBLE); // 인증 제한시간 표시
                    countDownTimer(); // 타이머 작동
                }
            }
        });


        join_sns_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // sns 무 선택 -> 입력창 비활성화
                if(checkedId == R.id.join_sns_n) {
                    join_sns_text.setClickable(false);
                    join_sns_text.setFocusable(false);
                }
                // sns 유 선택 -> 입력창 활성화
                else{
                    join_sns_text.setFocusableInTouchMode(true);
                    join_sns_text.setFocusable(true);
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

        // 인증번호 입력 -> 확인 버튼 눌렀을 시 발생
        join_check_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputAuth = join_.getText().toString();
                inputAuth = inputAuth.trim();

                // 인증번호 입력이 공백값일 경우
                if(inputAuth.getBytes().length <= 0){
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
    }

    public JSONObject getAuthData(){
        JSONObject item = new JSONObject();

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