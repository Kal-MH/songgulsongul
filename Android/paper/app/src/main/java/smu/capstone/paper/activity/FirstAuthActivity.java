package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.data.CodeResponse;
import smu.capstone.paper.data.IdData;
import smu.capstone.paper.data.LoginResponse;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;

public class FirstAuthActivity extends AppCompatActivity {

    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    private Intent intent;

    final int RESULT_OK = 200;
    final int RESULT_NO = 201;
    final int RESULT_CLIENT_ERR= 204;
    final int RESULT_SERVER_ERR = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        if(LoginSharedPreference.getLoginId(FirstAuthActivity.this).length() == 0) {
            // 로그인 기록 저장 x --> 로그인 화면으로
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
        }
        else { // 로그인 저장되어있음 -> 서버통신없이 자동 로그인
            Log.d("login", LoginSharedPreference.getLoginId(FirstAuthActivity.this)+"");
            Log.d("login", LoginSharedPreference.getUserId(FirstAuthActivity.this)+"");

            //출석확인
            IdData idData = new IdData(LoginSharedPreference.getUserId(FirstAuthActivity.this));
            serviceApi.Attendance(idData).enqueue( new Callback<CodeResponse>() {
                @Override
                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                    CodeResponse result = response.body();
                    int resultCode = result.getCode();
                    if (resultCode == RESULT_OK) { //첫출석이고 포인트올렸음
                        Toast.makeText(FirstAuthActivity.this, "출석체크 되었습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else if(resultCode == RESULT_NO){ //첫출석아님
                        Toast.makeText(FirstAuthActivity.this, "반갑습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else if(resultCode == RESULT_CLIENT_ERR){
                        // 없는 아이디가 저장됐다는것 만료된 아이디?.. 이럴일은 없지만그래두
                        Toast.makeText(FirstAuthActivity.this, "만료된 아이디입니다.", Toast.LENGTH_SHORT).show();
                        LoginSharedPreference.clearLogin(FirstAuthActivity.this); //로그아웃시키고
                        intent = new Intent(FirstAuthActivity.this, LoginActivity.class);//로그인창으로
                    }
                    else {
                        Toast.makeText(FirstAuthActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<CodeResponse> call, Throwable t) {
                    Toast.makeText(FirstAuthActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    Log.e("로그인 에러", t.getMessage());
                    t.printStackTrace(); // 에러 발생 원인 단계별로 출력

                }
            });



            intent = new Intent(FirstAuthActivity.this, HomeActivity.class);
            intent.putExtra("STD_NUM", LoginSharedPreference.getLoginId(this).toString());
        }
        startActivity(intent);
        this.finish();
    }
}