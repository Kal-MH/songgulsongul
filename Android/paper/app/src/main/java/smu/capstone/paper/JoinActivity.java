package smu.capstone.paper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    TextView join_timer;
    CountDownTimer countDownTimer;
    Button join_send_btn, join_check_key;
    ImageButton join_back_btn;
    RadioGroup join_sns_radio;
    EditText join_sns_text;

    final int MILLISINFUTURE = 180 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_send_btn = (Button)findViewById(R.id.join_send_btn);
        join_back_btn = (ImageButton)findViewById(R.id.join_back_btn);
        join_check_key = (Button)findViewById(R.id.join_check_key);
        join_timer = findViewById(R.id.join_timer);
        join_sns_radio = (RadioGroup)findViewById(R.id.join_sns_radio);
        join_sns_text = (EditText)findViewById(R.id.join_sns_text);

        join_check_key.setEnabled(false);
        join_timer.setVisibility(View.INVISIBLE); // 인증 제한시간 숨기기

        // 이메일 인증 버튼 눌렀을 시 발생
        join_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_check_key.setEnabled(true); // 인증번호 확인 버튼 활성화
                join_timer.setVisibility(View.VISIBLE); // 인증 제한시간 표시
                countDownTimer(); // 타이머 작동
            }
        });

        join_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                countDownTimer.cancel(); // 타이머 종료
            }
        });
    }

}
