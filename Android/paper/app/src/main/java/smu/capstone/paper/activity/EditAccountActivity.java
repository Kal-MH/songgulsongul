package smu.capstone.paper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.data.IdCheckData;
import smu.capstone.paper.data.PwEditData;
import smu.capstone.paper.fragment.FragEditId;
import smu.capstone.paper.fragment.FragEditPw;
import smu.capstone.paper.fragment.FragFindId;
import smu.capstone.paper.fragment.FragFindPw;
import smu.capstone.paper.responseData.CodeResponse;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class EditAccountActivity extends AppCompatActivity {
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    //private EditText account_newid, account_pw, account_newpw, account_newpw_check;
    private Button edit_id_btn, edit_pw_btn, edit_account_btn;
    private EditText account_newid, account_pw, account_newpw;

    int flag = 1;

    private final int Frag_editid = 1;
    private final int Frag_editpw = 2;

    String new_id, login_id, pw, new_pw;
    int user_id;
    int id_check, id_modify_check, pw_check, pw_check_flag;
    private int NO = 0;
    private int YES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_account_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("계정 관리");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        edit_id_btn = findViewById(R.id.edit_id_btn);
        edit_pw_btn = findViewById(R.id.edit_pw_btn);
        edit_account_btn = findViewById(R.id.edit_account_btn);

        login_id = LoginSharedPreference.getLoginId(EditAccountActivity.this);
        user_id = LoginSharedPreference.getUserId(EditAccountActivity.this);

        FragmentView(Frag_editid);
        SpannableString content = new SpannableString("ID 변경하기");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0); edit_id_btn.setText(content);
        edit_pw_btn.setText("PW 변경하기");

        edit_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1; // 아이디 변경 mode
                FragmentView(Frag_editid);

                SpannableString content = new SpannableString("ID 변경하기");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0); edit_id_btn.setText(content);
                edit_pw_btn.setText("PW 변경하기");

            }
        });

        edit_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2; // 비밀번호 변경 mode
                FragmentView(Frag_editpw);

                SpannableString content = new SpannableString("PW 변경하기");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0); edit_pw_btn.setText(content);
                edit_id_btn.setText("ID 변경하기");
            }
        });

        // 확인 버튼
        edit_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case 1: // 아이디 변경 mode
                        id_check = FragEditId.id_check;
                        id_modify_check = FragEditId.id_modify_check;
                        account_newid = findViewById(R.id.account_newid);
                        new_id = account_newid.getText().toString().trim();

                        if(new_id.getBytes().length <= 0){
                            new AlertDialog.Builder(EditAccountActivity.this)
                                    .setTitle("경고")
                                    .setMessage("변경할 아이디를 입력해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }

                        else if(id_check == NO){
                            new AlertDialog.Builder(EditAccountActivity.this)
                                    .setMessage("아이디 중복확인을 완료해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }

                        else if(id_check == YES){
                            serviceApi.IdChange(login_id, new_id).enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                    CodeResponse result = response.body();
                                    int resultCode = result.getCode();

                                    if(resultCode == StatusCode.RESULT_OK){
                                        Intent intent = new Intent(EditAccountActivity.this, ProfileActivity.class);
                                        LoginSharedPreference.changeLoginId(EditAccountActivity.this, new_id);

                                        Toast.makeText(EditAccountActivity.this, "아이디 변경 완료!", Toast.LENGTH_SHORT).show();
                                        intent.putExtra("userId", new_id);
                                        startActivity(intent);
                                        finish();
                                    }

                                    else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                                        new AlertDialog.Builder(EditAccountActivity.this)
                                                .setTitle("경고")
                                                .setMessage("Server Err."+"\n"+"다시 시도해주세요.")
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
                                    Toast.makeText(EditAccountActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    Log.e("아이디 변경 에러", t.getMessage());
                                    t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                }
                            });
                        }
                        break;

                    case 2: // 비밀번호 변경 mode
                        pw_check = FragEditPw.pw_check;
                        pw_check_flag = FragEditPw.pw_check_flag;
                        account_pw = findViewById(R.id.account_pw);
                        pw = account_pw.getText().toString().trim();
                        if(pw.getBytes().length <= 0){
                            new AlertDialog.Builder(EditAccountActivity.this)
                                    .setTitle("경고")
                                    .setMessage("현재 비밀번호를 입력해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }

                        else if(pw_check == NO){
                            new AlertDialog.Builder(EditAccountActivity.this)
                                    .setMessage("현재 비밀번호를 확인해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }

                        else if(pw_check_flag == NO){
                            new AlertDialog.Builder(EditAccountActivity.this)
                                    .setTitle("경고")
                                    .setMessage("새 비밀번호 확인을 완료해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }

                        else{
                            account_newpw = findViewById(R.id.account_newpw);
                            new_pw = account_newpw.getText().toString().trim();
                            PwEditData data = new PwEditData(user_id, new_pw);
                            serviceApi.PwChange(data).enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                    CodeResponse result = response.body();
                                    int resultCode = result.getCode();

                                    if(resultCode == StatusCode.RESULT_OK){
                                        LoginSharedPreference.clearLogin(EditAccountActivity.this);
                                        Intent intent = new Intent(EditAccountActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Toast.makeText(EditAccountActivity.this, "비밀번호 변경 완료!" + "\n"+"재로그인이 필요합니다.", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CodeResponse> call, Throwable t) {
                                    Toast.makeText(EditAccountActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    Log.e("비밀번호 변경 에러", t.getMessage());
                                    t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                }
                            });
                        }


                }

            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(EditAccountActivity.this, ProfileActivity.class);
        intent.putExtra("userId", login_id);
        startActivity(intent);
        finish();
    }


    private void FragmentView(int fragment){

        //FragmentTransactiom를 이용해 프래그먼트를 사용
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment){
            case 1:
                // 아이디 프래그먼트 호출
                FragEditId fragEditId = new FragEditId();
                transaction.replace(R.id.edit_account_frame, fragEditId);
                transaction.commit();
                break;

            case 2:
                // 비밀번호 프래그먼트 호출
                FragEditPw fragEditPw = new FragEditPw();
                transaction.replace(R.id.edit_account_frame, fragEditPw);
                transaction.commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                onBackPressed();
                break;
        }
        return true;
    }

}
