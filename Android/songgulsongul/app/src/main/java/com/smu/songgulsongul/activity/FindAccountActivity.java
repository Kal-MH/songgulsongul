package com.smu.songgulsongul.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.responseData.CodeResponse;
import com.smu.songgulsongul.data.EmailData;
import com.smu.songgulsongul.data.FindData;
import com.smu.songgulsongul.fragment.FragFindId;
import com.smu.songgulsongul.fragment.FragFindPw;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class FindAccountActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    Button find_id_btn, find_pw_btn, find_account_btn;
    int flag = 1; // 아이디 찾기 mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        find_id_btn = (Button) findViewById(R.id.find_id_btn);
        find_pw_btn = (Button) findViewById(R.id.find_pw_btn);
        find_account_btn = (Button) findViewById(R.id.find_account_btn);

        find_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1; // 아이디 찾기 mode

                SpannableString content = new SpannableString("ID 찾기");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                find_id_btn.setText(content);
                find_pw_btn.setText("비밀번호 찾기");

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


                SpannableString content = new SpannableString("비밀번호 찾기");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                find_pw_btn.setText(content);
                find_id_btn.setText("ID 찾기");

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

        SpannableString content = new SpannableString("ID 찾기");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        find_id_btn.setText(content);
        find_pw_btn.setText("비밀번호 찾기");


        // 확인 버튼
        find_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag) {
                    case 1: // 아이디 찾기 mode
                        final EditText find_id_email = findViewById(R.id.find_id_email);
                        String email = find_id_email.getText().toString();
                        email = email.trim(); // 공백값 허용x
                        Pattern email_pattern = Patterns.EMAIL_ADDRESS;

                        // 입력한 이메일이 공백값일 경우 --> 서버 통신x
                        if (email.getBytes().length <= 0) {

                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                            builder.setView(dialogView);

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            ImageView icon = dialogView.findViewById(R.id.warning);

                            TextView txt = dialogView.findViewById(R.id.txtText);
                            txt.setText("이메일을 입력해주세요.");

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

                        // 입력한 이메일이 형식에서 벗어날 경우 --> 서버 통신x
                        else if (!email_pattern.matcher(email).matches()) {

                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                            builder.setView(dialogView);

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            ImageView icon = dialogView.findViewById(R.id.warning);

                            TextView txt = dialogView.findViewById(R.id.txtText);
                            txt.setText("올바른 이메일 형식이 아닙니다.");

                            Button ok_btn = dialogView.findViewById(R.id.okBtn);
                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                            Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                            cancel_btn.setVisibility(View.GONE);
                        } else {
                            // 입력한 email로 server통신
                            EmailData data = new EmailData(email);
                            serviceApi.FindId(data).enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                    try {
                                        CodeResponse result = response.body();
                                        int resultCode = result.getCode();
                                        Log.d("code", String.valueOf(resultCode));

                                        if (resultCode == StatusCode.RESULT_OK) {

                                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                                            builder.setView(dialogView);

                                            final AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            ImageView icon = dialogView.findViewById(R.id.warning);
                                            icon.setVisibility(View.GONE);

                                            TextView txt = dialogView.findViewById(R.id.txtText);
                                            txt.setText("이메일로 아이디를 전송하였습니다.");

                                            Button ok_btn = dialogView.findViewById(R.id.okBtn);
                                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    find_id_email.setText(null);
                                                    alertDialog.dismiss();
                                                }
                                            });

                                            Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                                            cancel_btn.setVisibility(View.GONE);

                                        } else if (resultCode == StatusCode.RESULT_CLIENT_ERR) {

                                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                                            builder.setView(dialogView);

                                            final AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            ImageView icon = dialogView.findViewById(R.id.warning);

                                            TextView txt = dialogView.findViewById(R.id.txtText);
                                            txt.setText("가입되지 않은 이메일입니다.");

                                            Button ok_btn = dialogView.findViewById(R.id.okBtn);
                                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    find_id_email.setText(null);
                                                    alertDialog.dismiss();
                                                }
                                            });

                                            Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                                            cancel_btn.setVisibility(View.GONE);
                                        } else if (resultCode == StatusCode.RESULT_SERVER_ERR) {

                                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                                            builder.setView(dialogView);

                                            final AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            ImageView icon = dialogView.findViewById(R.id.warning);

                                            TextView txt = dialogView.findViewById(R.id.txtText);
                                            txt.setText("에러가 발생했습니다." + "\n" + "다시 시도해주세요.");

                                            Button ok_btn = dialogView.findViewById(R.id.okBtn);
                                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    find_id_email.setText(null);
                                                    alertDialog.dismiss();
                                                }
                                            });

                                            Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                                            cancel_btn.setVisibility(View.GONE);
                                        } else {
                                            Toasty.normal(FindAccountActivity.this, "서버와의 통신이 불안정합니다").show();
                                        }
                                    } catch (NullPointerException e) {

                                        View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                                        builder.setView(dialogView);

                                        final AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                        ImageView icon = dialogView.findViewById(R.id.warning);

                                        TextView txt = dialogView.findViewById(R.id.txtText);
                                        txt.setText("에러가 발생했습니다." + "\n" + "다시 시도해주세요.");

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
                                }

                                @Override
                                public void onFailure(Call<CodeResponse> call, Throwable t) {
                                    Toasty.normal(FindAccountActivity.this, "서버와의 통신이 불안정합니다").show();
                                    Log.e("아이디 찾기 에러", t.getMessage());
                                    t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                }
                            });
                        }
                        break;
                    case 2: // 비밀번호 찾기 mode
                        final EditText find_pw_id = (EditText) findViewById(R.id.find_pw_id);
                        final EditText find_pw_email = (EditText) findViewById(R.id.find_pw_email);
                        String id = find_pw_id.getText().toString();
                        String pw_email = find_pw_email.getText().toString();
                        id = id.trim();
                        pw_email = pw_email.trim();
                        Pattern pw_email_pattern = Patterns.EMAIL_ADDRESS;

                        // 입력한 이메일 또는 아이디가 공백값일 경우 --> 서버 통신x
                        if (id.getBytes().length <= 0 || pw_email.getBytes().length <= 0) {

                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                            builder.setView(dialogView);

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            ImageView icon = dialogView.findViewById(R.id.warning);

                            TextView txt = dialogView.findViewById(R.id.txtText);
                            txt.setText("미입력된 값을 입력해주세요.");

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

                        // 입력한 이메일이 형식에서 벗어날 경우 --> 서버 통신x
                        else if (!pw_email_pattern.matcher(pw_email).matches()) {

                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                            builder.setView(dialogView);

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            ImageView icon = dialogView.findViewById(R.id.warning);

                            TextView txt = dialogView.findViewById(R.id.txtText);
                            txt.setText("올바른 이메일 형식이 아닙니다.");

                            Button ok_btn = dialogView.findViewById(R.id.okBtn);
                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                            Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                            cancel_btn.setVisibility(View.GONE);
                        } else {
                            // id, email로 서버와 통신
                            FindData data = new FindData(pw_email, id);
                            serviceApi.FindPw(data).enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                    try {
                                        CodeResponse result = response.body();
                                        int resultCode = result.getCode();

                                        if (resultCode == StatusCode.RESULT_OK) {

                                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                                            builder.setView(dialogView);

                                            final AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            ImageView icon = dialogView.findViewById(R.id.warning);
                                            icon.setVisibility(View.GONE);

                                            TextView txt = dialogView.findViewById(R.id.txtText);
                                            txt.setText("이메일로 임시 비밀번호를 전송하였습니다.");

                                            Button ok_btn = dialogView.findViewById(R.id.okBtn);
                                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.dismiss();
                                                }
                                            });

                                            Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                                            cancel_btn.setVisibility(View.GONE);
                                        } else if (resultCode == StatusCode.RESULT_CLIENT_ERR) {
                                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                                            builder.setView(dialogView);

                                            final AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            ImageView icon = dialogView.findViewById(R.id.warning);

                                            TextView txt = dialogView.findViewById(R.id.txtText);
                                            txt.setText("존재하지 않는 정보입니다." + "\n" + "이메일 또는 아이디를 확인해주세요.");

                                            Button ok_btn = dialogView.findViewById(R.id.okBtn);
                                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    find_pw_id.setText(null);
                                                    find_pw_email.setText(null);
                                                    alertDialog.dismiss();
                                                }
                                            });

                                            Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                                            cancel_btn.setVisibility(View.GONE);
                                        } else if (resultCode == StatusCode.RESULT_SERVER_ERR) {

                                            View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                                            builder.setView(dialogView);

                                            final AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            ImageView icon = dialogView.findViewById(R.id.warning);

                                            TextView txt = dialogView.findViewById(R.id.txtText);
                                            txt.setText("에러가 발생했습니다." + "\n" + "다시 시도해주세요.");

                                            Button ok_btn = dialogView.findViewById(R.id.okBtn);
                                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    find_pw_id.setText(null);
                                                    find_pw_email.setText(null);
                                                    alertDialog.dismiss();
                                                }
                                            });

                                            Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                                            cancel_btn.setVisibility(View.GONE);
                                        } else {
                                            Toasty.normal(FindAccountActivity.this, "서버와의 통신이 불안정합니다").show();
                                        }
                                    } catch (NullPointerException e) {

                                        View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(FindAccountActivity.this);
                                        builder.setView(dialogView);

                                        final AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                        ImageView icon = dialogView.findViewById(R.id.warning);

                                        TextView txt = dialogView.findViewById(R.id.txtText);
                                        txt.setText("에러가 발생했습니다." + "\n" + "다시 시도해주세요.");

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
                                }

                                @Override
                                public void onFailure(Call<CodeResponse> call, Throwable t) {
                                    Toasty.normal(FindAccountActivity.this, "서버와의 통신이 불안정합니다").show();
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
        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
