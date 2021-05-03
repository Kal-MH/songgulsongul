package smu.capstone.paper.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.R;
import smu.capstone.paper.data.CodeResponse;
import smu.capstone.paper.data.EmailData;
import smu.capstone.paper.data.FindData;
import smu.capstone.paper.fragment.FragFindId;
import smu.capstone.paper.fragment.FragFindPw;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;

public class FindAccountActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    Button find_id_btn, find_pw_btn, find_account_btn;
    int flag = 1; // 아이디 찾기 mode

    final int RESULT_OK = 200;
    final int RESULT_CLIENT_ERR= 204;
    final int RESULT_SERVER_ERR = 500;

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
                flag = 1; // 아이디 찾기 mode
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragFindId fragFindId = new FragFindId();
                transaction.replace(R.id.find_frame, fragFindId);
                transaction.commit();
            }
        });

        find_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2; // 비밀번호 찾기 mode
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

        // 확인 버튼
        find_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case 1: // 아이디 찾기 mode
                        final EditText find_id_email = findViewById(R.id.find_id_email);
                        String email = find_id_email.getText().toString();
                        email = email.trim(); // 공백값 허용x
                        Pattern email_pattern = Patterns.EMAIL_ADDRESS;

                        // 입력한 이메일이 공백값일 경우 --> 서버 통신x
                        if(email.getBytes().length <= 0){
                            new AlertDialog.Builder(FindAccountActivity.this)
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
                            new AlertDialog.Builder(FindAccountActivity.this)
                                    .setTitle("경고")
                                    .setMessage("올바른 이메일 형식이 아닙니다.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }

                        else{
                            // 입력한 email로 server통신
                            EmailData data = new EmailData(email);
                            serviceApi.FindId(data).enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                    try {
                                        CodeResponse result = response.body();
                                        int resultCode = result.getCode();

                                        if (resultCode == RESULT_OK) {
                                            new AlertDialog.Builder(FindAccountActivity.this)
                                                    .setMessage("이메일로 아이디를 전송하였습니다.")
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            find_id_email.setText(null);
                                                        }
                                                    })
                                                    .show();
                                        } else if (resultCode == RESULT_CLIENT_ERR) {
                                            new AlertDialog.Builder(FindAccountActivity.this)
                                                    .setTitle("경고")
                                                    .setMessage("가입되지 않은 이메일입니다.")
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            find_id_email.setText(null);
                                                        }
                                                    })
                                                    .show();
                                        } else if (resultCode == RESULT_SERVER_ERR) {
                                            new AlertDialog.Builder(FindAccountActivity.this)
                                                    .setTitle("경고")
                                                    .setMessage("에러가 발생했습니다." + "\n" + "다시 시도해주세요.")
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            find_id_email.setText(null);
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            Toast.makeText(FindAccountActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (NullPointerException e){
                                        new AlertDialog.Builder(FindAccountActivity.this)
                                                .setTitle("경고")
                                                .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CodeResponse> call, Throwable t) {
                                    Toast.makeText(FindAccountActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    Log.e("아이디 찾기 에러", t.getMessage());
                                    t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                }
                            });
                        }
                        break;
                    case 2: // 비밀번호 찾기 mode
                        final EditText find_pw_id = (EditText)findViewById(R.id.find_pw_id);
                        final EditText find_pw_email = (EditText)findViewById(R.id.find_pw_email);
                        String id = find_pw_id.getText().toString();
                        String pw_email = find_pw_email.getText().toString();
                        id = id.trim();
                        pw_email = pw_email.trim();
                        Pattern pw_email_pattern = Patterns.EMAIL_ADDRESS;

                        // 입력한 이메일 또는 아이디가 공백값일 경우 --> 서버 통신x
                        if(id.getBytes().length <= 0 || pw_email.getBytes().length <= 0){
                            new AlertDialog.Builder(FindAccountActivity.this)
                                    .setTitle("경고")
                                    .setMessage("미입력된 값을 입력해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }

                        // 입력한 이메일이 형식에서 벗어날 경우 --> 서버 통신x
                        else if(!pw_email_pattern.matcher(pw_email).matches()){
                            new AlertDialog.Builder(FindAccountActivity.this)
                                    .setTitle("경고")
                                    .setMessage("올바른 이메일 형식이 아닙니다.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }

                        else{
                            // id, email로 서버와 통신
                            FindData data = new FindData(id, pw_email);
                            serviceApi.FindPw(data).enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                    try {
                                        CodeResponse result = response.body();
                                        int resultCode = result.getCode();

                                        if (resultCode == RESULT_OK) {
                                            new AlertDialog.Builder(FindAccountActivity.this)
                                                    .setMessage("이메일로 임시 비밀번호를 전송하였습니다.")
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .show();
                                        } else if (resultCode == RESULT_CLIENT_ERR) {
                                            new AlertDialog.Builder(FindAccountActivity.this)
                                                    .setTitle("경고")
                                                    .setMessage("존재하지 않는 정보입니다." + "\n" + "이메일 또는 아이디를 확인해주세요.")
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            find_pw_id.setText(null);
                                                            find_pw_email.setText(null);
                                                        }
                                                    })
                                                    .show();
                                        } else if (resultCode == RESULT_SERVER_ERR) {
                                            new AlertDialog.Builder(FindAccountActivity.this)
                                                    .setTitle("경고")
                                                    .setMessage("에러가 발생했습니다." + "\n" + "다시 시도해주세요.")
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            find_pw_id.setText(null);
                                                            find_pw_email.setText(null);
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            Toast.makeText(FindAccountActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (NullPointerException e){
                                        new AlertDialog.Builder(FindAccountActivity.this)
                                                .setTitle("경고")
                                                .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CodeResponse> call, Throwable t) {
                                    Toast.makeText(FindAccountActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    Log.e("비밀번호 찾기 에러", t.getMessage());
                                    t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                }
                            });
                        }
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