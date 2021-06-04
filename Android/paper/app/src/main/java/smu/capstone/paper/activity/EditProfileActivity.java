package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.data.IdCheckData;
import smu.capstone.paper.data.ProfileEditData;
import smu.capstone.paper.data.UserData;
import smu.capstone.paper.responseData.CodeResponse;
import smu.capstone.paper.responseData.ProfileResponse;
import smu.capstone.paper.responseData.User;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class EditProfileActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    private RadioGroup sns_radio;
    private RadioButton profile_sns_radio_yes, profile_sns_radio_no;
    private EditText profile_new_sns, profile_newid, profile_new_intro;
    private Button profile_img_chnage, profile_check, profile_delete_account;
    private ImageView profile_set_img;
    private static final int REQUEST_CODE = 0;
    private int profile_sns_check;
    private String login_id, new_id, profile_img_old, profile_img_path;
    private int id_check, id_modify_check, profile_modify_check;
    private int NO = 0;
    private int YES = 1;

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
        id_check = NO;
        id_modify_check = NO;

        profile_new_intro = (EditText)findViewById(R.id.profile_new_intro);
        profile_new_sns = (EditText)findViewById(R.id.profile_new_sns);
        profile_img_chnage = findViewById(R.id.profile_img_chnage);
        profile_set_img = findViewById(R.id.profile_set_img);
        sns_radio = findViewById(R.id.profile_edit_sns_radio);
        profile_newid = findViewById(R.id.profile_newid);
        profile_sns_radio_yes = findViewById(R.id.profile_sns_radio_yes);
        profile_sns_radio_no = findViewById(R.id.profile_sns_radio_no);
        profile_check = findViewById(R.id.profile_check);
        profile_delete_account = findViewById(R.id.profile_delete_account);

        getProfileData();

        profile_img_chnage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        profile_newid.setText(LoginSharedPreference.getLoginId(this));

        profile_newid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                id_check = NO;
                id_modify_check = NO;
            }
        });

        profile_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_id = profile_newid.getText().toString();
                new_id.trim();

                // 입력값이 공백일 경우 --> 서버 통신x
                if(new_id.getBytes().length <= 0){
                    id_check = NO;
                    id_modify_check = NO;
                    new AlertDialog.Builder(EditProfileActivity.this)
                            .setTitle("경고")
                            .setMessage("변경할 아이디를 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                // 현재 사용중인 id와 동일한 id 입력시 --> 서버 통신x
                else if (login_id.equals(new_id)){
                    new AlertDialog.Builder(EditProfileActivity.this)
                            .setMessage("기존 아이디를 사용하시겠습니까?")
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    profile_newid.setText(null);
                                    id_check = NO;
                                    id_modify_check = NO;
                                }
                            })
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    id_check = YES;
                                    id_modify_check = NO;
                                }
                            })
                            .show();
                }

                // new_id로 server와 통신
                else {
                    IdCheckData data = new IdCheckData(new_id);
                    serviceApi.IdCheck(data).enqueue(new Callback<CodeResponse>() {
                        @Override
                        public void onResponse(Call<CodeResponse>call, Response<CodeResponse> response) {
                            CodeResponse result = response.body();
                            int resultCode = result.getCode();

                            if(resultCode == StatusCode.RESULT_OK){
                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                                id_check = YES;
                                id_modify_check = YES;
                            }

                            else if (resultCode == StatusCode.RESULT_CLIENT_ERR){
                                id_check = NO;
                                id_modify_check = NO;
                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setTitle("경고")
                                        .setMessage("이미 사용중인 아이디입니다."+"\n"+"다시 입력해 주세요.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                profile_newid.setText(null);
                                            }
                                        })
                                        .show();
                            }

                            else if (resultCode == StatusCode.RESULT_SERVER_ERR){
                                id_check = NO;
                                id_modify_check = NO;
                                new AlertDialog.Builder(EditProfileActivity.this)
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
                            id_check = NO;
                            id_modify_check = NO;
                            Toast.makeText(EditProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                            Log.e("아이디 중복확인 에러", t.getMessage());
                            t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                        }
                    });
                }
            }
        });

        sns_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // sns 무 선택 -> 입력창 비활성화

                if(checkedId == R.id.profile_sns_radio_no) {
                    profile_new_sns.setClickable(false);
                    profile_new_sns.setFocusable(false);
                    profile_new_sns.setHint("sns 계정 없음");
                    profile_new_sns.setText("");
                }
                // sns 유 선택 -> 입력창 활성화
                else{
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

                new AlertDialog.Builder(EditProfileActivity.this)
                        .setMessage("정말 탈퇴 하시겠습니까?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                            new AlertDialog.Builder(EditProfileActivity.this)
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
                                        Toast.makeText(EditProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                        Log.e("회원탈퇴 에러", t.getMessage());
                                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                    }
                                });
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        intent.putExtra("userId", login_id);
        startActivity(intent);
        finish();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    profile_set_img.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

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

                if(sns_radio.getCheckedRadioButtonId() == R.id.profile_sns_radio_no)
                    profile_sns_check = NO;
                else if(sns_radio.getCheckedRadioButtonId() == R.id.profile_sns_radio_yes)
                    profile_sns_check = YES;

                if(!profile_img_old.equals(profile_img_path)) {
                    profile_modify_check = YES;
                    File file = new File(profile_img_path);
                    requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                    body = MultipartBody.Part.createFormData("img_profile", file.getName(), requestFile);
                }
                else {
                    profile_modify_check = NO;
                    requestFile = RequestBody.create(MediaType.parse("image/jpeg"), profile_img_path);
                    body = MultipartBody.Part.createFormData("img_profile", profile_img_path);
                }

                String new_sns = profile_new_sns.getText().toString().trim();

                HashMap<String, RequestBody> requestMap = getMapData(id_modify_check, profile_sns_check, profile_modify_check, login_id, new_intro, new_sns);

                if(id_check == NO){
                    new AlertDialog.Builder(EditProfileActivity.this)
                            .setMessage("아이디 중복확인을 완료해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
                else if(profile_sns_check == YES && new_sns.getBytes().length <= 0){
                        new AlertDialog.Builder(EditProfileActivity.this)
                                .setMessage("SNS계정을 입력해주세요.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                }
                else if(id_check == YES){
                    if(id_modify_check == YES) {
                        RequestBody new_id_body = RequestBody.create(MediaType.parse("text/plain"), new_id);
                        requestMap.put("new_id", new_id_body);
                    }
                    serviceApi.EditProfile(requestMap, body).enqueue(new Callback<CodeResponse>() {
                        @Override
                        public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                            CodeResponse result = response.body();
                            int resultCode = result.getCode();

                            if(resultCode == StatusCode.RESULT_OK){
                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                if(id_modify_check == YES) {
                                    LoginSharedPreference.changeLoginId(EditProfileActivity.this, new_id);
                                }
                                intent.putExtra("userId", new_id);
                                startActivity(intent);
                                finish();
                                Toast.makeText(EditProfileActivity.this, "프로필 수정 완료!", Toast.LENGTH_SHORT).show();
                            }
                            else if(resultCode == StatusCode.RESULT_CLIENT_ERR){
                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setTitle("경고")
                                        .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                            }
                            else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                                new AlertDialog.Builder(EditProfileActivity.this)
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
                            Toast.makeText(EditProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
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
        return  true;
    }

    public HashMap<String, RequestBody> getMapData(int id_modify_check, int profile_sns_check, int profile_modify_check, String login_id, String new_intro, String new_SNS){
        HashMap<String, RequestBody> requestMap = new HashMap<>();

        RequestBody id_flag_body = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id_modify_check));
        RequestBody sns_flag_body = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(profile_sns_check));
        RequestBody profile_flag_body = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(profile_modify_check));
        RequestBody login_id_body = RequestBody.create(MediaType.parse("text/plain"), login_id);
        RequestBody new_intro_body = RequestBody.create(MediaType.parse("text/plain"), new_intro);
        RequestBody new_sns_body = RequestBody.create(MediaType.parse("text/plain"), new_SNS);

        requestMap.put("id_check_flag", id_flag_body);
        requestMap.put("sns_check_flag", sns_flag_body);
        requestMap.put("img_check_flag", profile_flag_body);
        requestMap.put("login_id", login_id_body);
        requestMap.put("new_intro", new_intro_body);
        requestMap.put("new_SNS", new_sns_body);
        return requestMap;
    }

    //server에서 data전달
    public void getProfileData(){
        UserData data = new UserData(login_id, 1);
        serviceApi.Profile(data).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse result = response.body();
                int resultCode = result.getCode();

                if(resultCode == StatusCode.RESULT_OK){
                    setProfileData(result);
                }
                else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                    new AlertDialog.Builder(EditProfileActivity.this)
                            .setTitle("경고")
                            .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 에러 발생 시 새로고침
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                Log.e("프로필 데이터 불러오기 에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
    }

    public void setProfileData(ProfileResponse data){
        User user = data.getProfileInfo();


        profile_new_intro.setText(user.getIntro());
        profile_new_sns.setText(user.getSns());

        profile_img_old =  user.getImg_profile();
        profile_img_path = profile_img_old;

        String img_addr = profile_img_old;
        String base_url = RetrofitClient.getBaseUrl();

        Glide.with(this).load(base_url + img_addr).into(profile_set_img);
        int sns_check = user.getSnsCheck();

        if(sns_check == NO){
                profile_sns_radio_no.setChecked(true);
                profile_sns_radio_yes.setChecked(false);
                profile_new_sns.setClickable(false);
                profile_new_sns.setFocusable(false);
                profile_new_sns.setHint("sns 계정 없음");
                profile_new_sns.setText("");
            }
        else{
                profile_sns_radio_yes.setChecked(true);
                profile_sns_radio_no.setChecked(false);
                profile_new_sns.setFocusableInTouchMode(true);
                profile_new_sns.setFocusable(true);
                profile_new_sns.setHint("sns 계정을 입력하세요");
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Uri uri = null;
                if(data != null){
                    uri = data.getData();
                }
                if(uri != null){
                    profile_set_img.setImageURI(uri);
                    profile_img_path = createCopyAndReturnRealPath(this, uri);
                    Log.d("img_path", profile_img_path);
                }
            }
        }
    }

    @Nullable
    public static String createCopyAndReturnRealPath(@NonNull Context context, @NonNull Uri uri){
        final ContentResolver contentResolver = context.getContentResolver();

        if(contentResolver == null)
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
        } catch (IOException e){
            return null;
        }
        return file.getAbsolutePath();
    }

}