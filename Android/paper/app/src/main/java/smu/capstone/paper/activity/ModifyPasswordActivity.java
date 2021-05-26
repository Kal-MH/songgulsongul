package smu.capstone.paper.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import smu.capstone.paper.LoginSharedPreference;

import smu.capstone.paper.R;

public class ModifyPasswordActivity extends AppCompatActivity {

    TextView current_id;
    EditText current_pw, modify_pw, modify_pw_check;
    Button modify_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pw);

        current_id = (TextView)findViewById(R.id.current_id);
        current_pw = (EditText)findViewById(R.id.current_pw);
        modify_pw = (EditText)findViewById(R.id.mod_pw);
        modify_pw_check = (EditText)findViewById(R.id.mod_pw_check);
        modify_btn = (Button)findViewById(R.id.modify_btn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mod_pw_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("비밀번호 변경");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        //LoginSharedPreference에서 가져온 현재 로그인 데이터
        String prf_id = LoginSharedPreference.getLoginId(this);
        String prf_pw = "";

        //현재 로그인 된 아이디를 보여줌
        current_id.setText(prf_id);

        modify_btn.setOnClickListener(new View.OnClickListener(){ //변경 버튼 클릭 시
            @Override
            public void onClick(View v) {
                String cur_pw = current_pw.getText().toString();
                String mod_pw = modify_pw.getText().toString();
                String mod_pw_check = modify_pw_check.getText().toString();
                String prf_pw = "";

                //공백 금지
                cur_pw.trim();
                mod_pw.trim();
                mod_pw_check.trim();

                //현재 패스워드 일치 확인
                if(cur_pw != prf_pw){   //불일치
                    new AlertDialog.Builder(ModifyPasswordActivity.this)
                            .setTitle("경고")
                            .setMessage("현재 패스워드와 일치하지 않습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
                else{   //일치
                    //변경할 패스워드 일치 확인
                    if(mod_pw != mod_pw_check){   //불일치
                        new AlertDialog.Builder(ModifyPasswordActivity.this)
                                .setTitle("경고")
                                .setMessage("패스워드가 일치하지 않습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                    else{       //일치
                        new AlertDialog.Builder(ModifyPasswordActivity.this)
                                .setMessage("패스워드가 변경되었습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                Log.d("TAG", "뒤로,,,");
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
