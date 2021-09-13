package com.smu.songgulsongul.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.data.UserData;
import com.smu.songgulsongul.responseData.CodeResponse;
import com.smu.songgulsongul.responseData.ProfileResponse;
import com.smu.songgulsongul.responseData.User;
import com.smu.songgulsongul.server.DefaultImage;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class EditProfileActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    private RadioGroup sns_radio;
    private RadioButton profile_sns_radio_yes, profile_sns_radio_no;
    private EditText profile_new_sns, profile_new_intro;
    private Button profile_img_change, profile_delete_account;
    private TextView profile_edit_id;
    private ImageView profile_set_img;
    private static final int REQUEST_CODE = 0;
    private int profile_sns_check;
    private String login_id, profile_img_old, profile_img_path;
    private int profile_modify_check;
    private int NO = 0;
    private int YES = 1;

    int BackColor = Color.parseColor("#BFB1D8");
    int FontColor = Color.parseColor("#000000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("프로필 수정");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        login_id = LoginSharedPreference.getLoginId(EditProfileActivity.this);

        profile_new_intro = (EditText) findViewById(R.id.profile_new_intro);
        profile_new_sns = (EditText) findViewById(R.id.profile_new_sns);
        profile_edit_id = findViewById(R.id.profile_edit_id);
        profile_img_change = findViewById(R.id.profile_img_chnage);
        profile_set_img = findViewById(R.id.profile_set_img);
        sns_radio = findViewById(R.id.profile_edit_sns_radio);
        profile_sns_radio_yes = findViewById(R.id.profile_sns_radio_yes);
        profile_sns_radio_no = findViewById(R.id.profile_sns_radio_no);
        profile_delete_account = findViewById(R.id.profile_delete_account);

        getProfileData();

        profile_img_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        sns_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // sns 무 선택 -> 입력창 비활성화

                if (checkedId == R.id.profile_sns_radio_no) {
                    profile_new_sns.setClickable(false);
                    profile_new_sns.setFocusable(false);
                    profile_new_sns.setHint("sns 계정 없음");
                    profile_new_sns.setText("");
                }
                // sns 유 선택 -> 입력창 활성화
                else {
                    profile_new_sns.setFocusableInTouchMode(true);
                    profile_new_sns.setFocusable(true);
                    profile_new_sns.setHint("sns 계정을 입력하세요");
                }
            }
        });

        profile_delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserData data = new UserData(login_id, 1);

                View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView icon=dialogView.findViewById(R.id.warning);
                icon.setVisibility(View.GONE);

                TextView txt=dialogView.findViewById(R.id.txtText);
                txt.setText("정말 탈퇴하시겠습니까?");
                Button ok_btn = dialogView.findViewById(R.id.okBtn);
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviceApi.DeleteAccount(data).enqueue(new Callback<CodeResponse>() {
                            @Override
                            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                CodeResponse result = response.body();
                                int resultCode = result.getCode();
                                if(resultCode == StatusCode.RESULT_OK){
                                    Toast.makeText(EditProfileActivity.this, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    LoginSharedPreference.clearLogin(EditProfileActivity.this);
                                    Intent intent3 = new Intent(EditProfileActivity.this, LoginActivity.class);
                                    intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent3);
                                }
                                else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                                    View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                                    builder.setView(dialogView);
                                    final AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    ImageView icon=dialogView.findViewById(R.id.warning);

                                    TextView txt=dialogView.findViewById(R.id.txtText);
                                    txt.setText("에러가 발생했습니다."+"\n"+"다시 시도해주세요.");
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
                                Toast.makeText(EditProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                Log.e("회원탈퇴 에러", t.getMessage());
                                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                            }
                        });
                        alertDialog.dismiss();
                    }
                });
                Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        intent.putExtra("userId", login_id);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.toolbar_save:
                String new_intro = profile_new_intro.getText().toString().trim();
                RequestBody requestFile;
                MultipartBody.Part body;

                if (sns_radio.getCheckedRadioButtonId() == R.id.profile_sns_radio_no)
                    profile_sns_check = NO;
                else if (sns_radio.getCheckedRadioButtonId() == R.id.profile_sns_radio_yes)
                    profile_sns_check = YES;

                String new_sns = profile_new_sns.getText().toString().trim();

                HashMap<String, RequestBody> requestMap = getMapData(profile_sns_check, profile_modify_check, login_id, new_intro, new_sns);

                if (!profile_img_old.equals(profile_img_path)) {
                    profile_modify_check = YES;
                    File file = new File(profile_img_path); // 변경한 프로필 이미지
                    requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                    body = MultipartBody.Part.createFormData("img_profile", file.getName(), requestFile);

                    // 기존 프로필 이미지
                    RequestBody old_profile_body = RequestBody.create(MediaType.parse("text/plain"), profile_img_old);
                    requestMap.put("old_profile_img", old_profile_body);
                } else {
                    profile_modify_check = NO;
                    requestFile = RequestBody.create(MediaType.parse("image/jpeg"), profile_img_path);
                    body = MultipartBody.Part.createFormData("img_profile", profile_img_path);
                }

                if (profile_sns_check == YES && new_sns.getBytes().length <= 0) {
                    View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ImageView icon=dialogView.findViewById(R.id.warning);
                    icon.setVisibility(View.GONE);

                    TextView txt=dialogView.findViewById(R.id.txtText);
                    txt.setText("SNS계정을 입력해주세요.");
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
                    serviceApi.EditProfile(requestMap, body).enqueue(new Callback<CodeResponse>() {
                        @Override
                        public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                            CodeResponse result = response.body();
                            int resultCode = result.getCode();

                            if (resultCode == StatusCode.RESULT_OK) {
                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                intent.putExtra("userId", login_id);
                                startActivity(intent);
                                finish();
                                Toasty.custom(EditProfileActivity.this, "프로필 수정 완료!", null, BackColor, FontColor, 2000, false, true).show();
                            } else if (resultCode == StatusCode.RESULT_CLIENT_ERR) {
                                View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                                builder.setView(dialogView);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                ImageView icon=dialogView.findViewById(R.id.warning);

                                TextView txt=dialogView.findViewById(R.id.txtText);
                                txt.setText("에러가 발생했습니다"+"\n"+"다시 시도해주세요.");
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                                builder.setView(dialogView);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                ImageView icon=dialogView.findViewById(R.id.warning);

                                TextView txt=dialogView.findViewById(R.id.txtText);
                                txt.setText("Server Err."+"\n"+"다시 시도해주세요.");
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
                            Toasty.normal(EditProfileActivity.this, "서버와의 통신이 불안정합니다.").show();
                            Log.e("프로필 수정 에러", t.getMessage());
                            t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                        }
                    });

                }
                return true;

            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                onBackPressed();
                break;
        }
        return true;
    }

    public HashMap<String, RequestBody> getMapData(int profile_sns_check, int profile_modify_check, String login_id, String new_intro, String new_SNS) {
        HashMap<String, RequestBody> requestMap = new HashMap<>();

        RequestBody sns_flag_body = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(profile_sns_check));
        RequestBody profile_flag_body = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(profile_modify_check));
        RequestBody login_id_body = RequestBody.create(MediaType.parse("text/plain"), login_id);
        RequestBody new_intro_body = RequestBody.create(MediaType.parse("text/plain"), new_intro);
        RequestBody new_sns_body = RequestBody.create(MediaType.parse("text/plain"), new_SNS);

        requestMap.put("sns_check_flag", sns_flag_body);
        requestMap.put("img_check_flag", profile_flag_body);
        requestMap.put("login_id", login_id_body);
        requestMap.put("new_intro", new_intro_body);
        requestMap.put("new_SNS", new_sns_body);
        return requestMap;
    }

    //server에서 data전달
    public void getProfileData() {
        UserData data = new UserData(login_id, 1);
        serviceApi.Profile(data).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse result = response.body();
                int resultCode = result.getCode();

                if (resultCode == StatusCode.RESULT_OK) {
                    setProfileData(result);
                } else if (resultCode == StatusCode.RESULT_SERVER_ERR) {
                    View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ImageView icon=dialogView.findViewById(R.id.warning);

                    TextView txt=dialogView.findViewById(R.id.txtText);
                    txt.setText("에러가 발생했습니다"+"\n"+"다시 시도해주세요.");
                    Button ok_btn = dialogView.findViewById(R.id.okBtn);
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //에러 발생 시 새로고침
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                            alertDialog.dismiss();
                        }
                    });
                    Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                    cancel_btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toasty.normal(EditProfileActivity.this, "서버와의 통신이 불안정합니다.").show();
                Log.e("프로필 데이터 불러오기 에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
    }

    public void setProfileData(ProfileResponse data) {
        User user = data.getProfileInfo();

        profile_new_intro.setText(user.getIntro());
        profile_new_sns.setText(user.getSns());
        profile_edit_id.setText(user.getUserId());
        profile_img_old = user.getImg_profile();
        profile_img_path = profile_img_old;

        String img_addr;
        String base_url = RetrofitClient.getBaseUrl();

        if (profile_img_old.equals(DefaultImage.DEFAULT_IMAGE))
            img_addr = base_url + profile_img_old;
        else
            img_addr = profile_img_old;

        Glide.with(this).load(img_addr).into(profile_set_img);

        int sns_check = user.getSnsCheck();
        Log.d("sns_check", String.valueOf(sns_check));

        if (sns_check == NO) {
            profile_sns_radio_no.setChecked(true);
            profile_sns_radio_yes.setChecked(false);
            profile_new_sns.setClickable(false);
            profile_new_sns.setFocusable(false);
            profile_new_sns.setHint("sns 계정 없음");
            profile_new_sns.setText("");
        } else {
            profile_sns_radio_yes.setChecked(true);
            profile_sns_radio_no.setChecked(false);
            profile_new_sns.setFocusableInTouchMode(true);
            profile_new_sns.setFocusable(true);
            profile_new_sns.setHint("sns 계정을 입력하세요");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                if (uri != null) {
                    profile_set_img.setImageURI(uri);
                    profile_img_path = createCopyAndReturnRealPath(this, uri);
                    Log.d("img_path", profile_img_path);
                }
            }
        }
    }

    @Nullable
    public static String createCopyAndReturnRealPath(@NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();

        if (contentResolver == null)
            return null;

        String filePath = context.getApplicationInfo().dataDir + File.separator
                + System.currentTimeMillis();

        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            return null;
        }
        return file.getAbsolutePath();
    }

}