package com.smu.songgulsongul.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.data.CodeResponse;
import com.smu.songgulsongul.data.user.IdData;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class FirstAuthActivity extends AppCompatActivity {
    int BackColor = Color.parseColor("#BFB1D8");
    int FontColor = Color.parseColor("#000000");

    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    private Intent intent;

    private static final int MULTIPLE_PERMISSIONS = 101;
    private final String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // 기기, 사진, 미디어, 파일 엑세스 권한
            Manifest.permission.CAMERA
    };

    List<String> permissionList = new ArrayList<>();

    StatusCode statusCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        // 퍼미션 체킹
        if (Build.VERSION.SDK_INT >= 23) { // 안드로이드 6.0 이상일 경우 퍼미션 체크
            if (checkPermissions())
                doCheckAutoLogin();
            else
                ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
        } else // 6.0 이하 퍼미션 체크이유없음
            doCheckAutoLogin();


    }

    public void doCheckAutoLogin() {
        if (LoginSharedPreference.getLoginId(FirstAuthActivity.this).length() == 0) {
            // 로그인 기록 저장 x --> 로그인 화면으로
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
        } else { // 로그인 저장되어있음 -> 서버통신없이 자동 로그인
            Log.d("login", LoginSharedPreference.getLoginId(FirstAuthActivity.this) + "");
            Log.d("login", LoginSharedPreference.getUserId(FirstAuthActivity.this) + "");

            //출석확인
            IdData idData = new IdData(LoginSharedPreference.getUserId(FirstAuthActivity.this));
            serviceApi.Attendance(idData).enqueue(new Callback<CodeResponse>() {
                @Override
                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                    CodeResponse result = response.body();
                    int resultCode = result.getCode();
                    if (resultCode == StatusCode.RESULT_OK) { //첫출석이고 포인트올렸음
                        Toasty.custom(FirstAuthActivity.this, "출석체크 되었습니다!", null, BackColor, FontColor, 2000, false, true).show();
                    } else if (resultCode == StatusCode.RESULT_NO) { //첫출석아님
                        Toasty.custom(FirstAuthActivity.this, "반갑습니다!", null, BackColor, FontColor, 2000, false, true).show();
                    } else if (resultCode == StatusCode.RESULT_CLIENT_ERR) {
                        // 없는 아이디가 저장됐다는것 만료된 아이디?.. 이럴일은 없지만그래두
                        Toasty.normal(FirstAuthActivity.this, "만료된 아이디입니다").show();
                        LoginSharedPreference.clearLogin(FirstAuthActivity.this); //로그아웃시키고
                        intent = new Intent(FirstAuthActivity.this, LoginActivity.class);//로그인창으로
                    } else {
                        Toasty.normal(FirstAuthActivity.this, "서버와의 통신이 불안정합니다").show();
                    }

                }

                @Override
                public void onFailure(Call<CodeResponse> call, Throwable t) {
                    Toasty.normal(FirstAuthActivity.this, "서버와의 통신이 불안정합니다").show();
                    Log.e("로그인 에러", t.getMessage());
                    t.printStackTrace(); // 에러 발생 원인 단계별로 출력

                }
            });


            intent = new Intent(FirstAuthActivity.this, HomeActivity.class);
            intent.putExtra("STD_NUM", LoginSharedPreference.getLoginId(this));
        }
        startActivity(intent);
        this.finish();
    }

    private boolean checkPermissions() {
        int result;
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        return permissionList.isEmpty();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS) {
            for (int g : grantResults) {
                if (g == PackageManager.PERMISSION_DENIED) {
                    showToast_PermissionDeny();
                    return;
                }
            }
            doCheckAutoLogin();
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showToast_PermissionDeny() {
        ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
    }
}