package smu.capstone.paper.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import smu.capstone.paper.R;
import smu.capstone.paper.fragment.FragFindId;
import smu.capstone.paper.fragment.FragFindPw;

public class FindAccountActivity extends AppCompatActivity {

    Button find_id_btn, find_pw_btn, find_account_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        find_id_btn = (Button)findViewById(R.id.find_id_btn);
        find_pw_btn = (Button)findViewById(R.id.find_pw_btn);
        find_account_btn = (Button)findViewById(R.id.find_account_btn);

        find_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragFindId fragFindId = new FragFindId();
                transaction.replace(R.id.find_frame, fragFindId);
                transaction.commit();
            }
        });

        find_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragFindPw fragFindPw = new FragFindPw();
                transaction.replace(R.id.find_frame, fragFindPw);
                transaction.commit();

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.find_account_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setTitle("계정찾기");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragFindId fragFindId = new FragFindId();
        transaction.replace(R.id.find_frame, fragFindId);
        transaction.commit();

        //fragFindId = (FragFindId)getSupportFragmentManager().findFragmentById(R.id.find_frame);
        final EditText find_id_email = findViewById(R.id.find_id_email);

        find_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = find_id_email.getText().toString();
                if(email.getBytes().length <= 0){
                    new AlertDialog.Builder(FindAccountActivity.this)
                            .setTitle("경고")
                            .setMessage("이메일을 입력해주세요.")
                            .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                else{
                    // email주소 서버에 전송

                    // if resultCode == 200
                    new AlertDialog.Builder(FindAccountActivity.this)
                            .setMessage("이메일로 아이디를 전송하였습니다.")
                            .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                    // if resultCode == 204
                    new AlertDialog.Builder(FindAccountActivity.this)
                            .setMessage("존재하지 않는 정보입니다.")
                            .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });

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