package com.smu.songgulsongul.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.data.PwEditData;
import com.smu.songgulsongul.responseData.CodeResponse;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class FragEditPw extends Fragment {
    public static int pw_check = 0;
    public static int pw_check_flag = 0;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    private final int YES = 1;
    private final int NO = 0;

    EditText account_pw, account_newpw, account_newpw_check;
    TextView pw_check_text;
    Button account_pw_check;
    String pw;
    int user_id;

    public FragEditPw() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_edit_pw, container, false);

        account_pw = (EditText) rootView.findViewById(R.id.account_pw);
        account_newpw = (EditText) rootView.findViewById(R.id.account_newpw);
        account_newpw_check = (EditText) rootView.findViewById(R.id.account_newpw_check);
        pw_check_text = (TextView) rootView.findViewById(R.id.edit_account_pw_correct);
        account_pw_check = rootView.findViewById(R.id.edit_account_pw_check);
        user_id = LoginSharedPreference.getUserId(getContext());
        pw_check_text.setVisibility(View.INVISIBLE);

        account_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pw_check = NO;
            }
        });

        // 비밀번호 일치 확인
        account_newpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (account_newpw_check.getText().toString().trim().length() > 0) {
                    if (s.toString().equals(account_newpw_check.getText().toString())) {
                        pw_check_text.setText("비밀번호가 일치합니다.");
                        pw_check_text.setVisibility(View.VISIBLE);
                        pw_check_flag = YES;
                    } else {
                        pw_check_text.setText("비밀번호가 일치하지 않습니다.");
                        pw_check_text.setVisibility(View.VISIBLE);
                        pw_check_flag = NO;
                    }
                }
            }
        });

        account_newpw_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pw_check_text.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(account_newpw.getText().toString())) {
                    pw_check_text.setText("비밀번호가 일치합니다.");
                    pw_check_text.setVisibility(View.VISIBLE);
                    pw_check_flag = 1;
                } else {
                    pw_check_text.setText("비밀번호가 일치하지 않습니다.");
                    pw_check_text.setVisibility(View.VISIBLE);
                    pw_check_flag = 0;
                }
            }
        });


        account_pw_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw = account_pw.getText().toString().trim();
                if (pw.getBytes().length <= 0) {

                    View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                    Context context = container.getContext();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

                    pw_check = NO;
                } else {
                    PwEditData data = new PwEditData(user_id, pw);
                    serviceApi.PwCheck(data).enqueue(new Callback<CodeResponse>() {
                        @Override
                        public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                            CodeResponse result = response.body();
                            int resultCode = result.getCode();

                            if (resultCode == StatusCode.RESULT_OK) {
                                pw_check = YES;

                                View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                Context context = container.getContext();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setView(dialogView);

                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                ImageView icon = dialogView.findViewById(R.id.warning);

                                TextView txt = dialogView.findViewById(R.id.txtText);
                                txt.setText("비밀번호가 일치합니다.");

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
                                pw_check = NO;

                                View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                Context context = container.getContext();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setView(dialogView);

                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                ImageView icon = dialogView.findViewById(R.id.warning);

                                TextView txt = dialogView.findViewById(R.id.txtText);
                                txt.setText("비밀번호가 일치하지 않습니다.");

                                Button ok_btn = dialogView.findViewById(R.id.okBtn);
                                ok_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });

                                Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                                cancel_btn.setVisibility(View.GONE);
                            } else if (resultCode == StatusCode.RESULT_SERVER_ERR) {

                                View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                Context context = container.getContext();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                            Toasty.normal(getContext(), "서버와의 통신이 불안정합니다.").show();
                            Log.e("비밀번호 확인 에러", t.getMessage());
                            t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                        }
                    });
                }
            }
        });


        return rootView;

    }
}
