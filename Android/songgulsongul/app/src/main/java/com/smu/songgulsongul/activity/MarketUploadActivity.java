package com.smu.songgulsongul.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketUploadActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    String filePath, login_id;
    int user_id;
    ImageView market_upload_img;
    EditText market_upload_name, market_upload_price, market_upload_text;
    RequestBody requestFile, requestId, requestPrice, requestText;
    MultipartBody.Part imageBody;

    long first_time = 0;
    long second_time = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_upload);

        market_upload_name = findViewById(R.id.market_upload_name);
        market_upload_price = findViewById(R.id.market_upload_price);
        market_upload_text = findViewById(R.id.market_upload_text);

        market_upload_img = findViewById(R.id.market_upload_img);

        user_id = LoginSharedPreference.getUserId(this);
        login_id = LoginSharedPreference.getLoginId(this);

        // 툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.market_upload_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        //이미지 세팅
        filePath = getIntent().getStringExtra("path");
        Glide.with(this).load(filePath).into(market_upload_img);
    }


    // 서버에 전송할 데이터 묶기
    public void makeUploadData() {

        // 업로드 이미지
        File file = new File(filePath);
        requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        imageBody = MultipartBody.Part.createFormData("img_post", file.getName(), requestFile);

        // 사용자 아이디
        requestId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id));

        //판매 가격
        String price = market_upload_text.getText().toString().trim();
        requestPrice = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(price));

        // 상세 설명
        String text = market_upload_text.getText().toString().trim();
        requestText = RequestBody.create(MediaType.parse("text/plain"), text);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_toolbar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if(second_time-first_time <2000){
            super.onBackPressed();
            finish();
        }
        else{
            Toast.makeText(this,"한번 더 누르면 업로드를 종료합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                // 알림 팝업
                return true;

            case R.id.toolbar_done :
                makeUploadData();

                Toast toast = Toast.makeText(MarketUploadActivity.this, "업로드 완료", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(MarketUploadActivity.this, StickerDetailActivity.class); // 업로드 된 게시물로 이동 (게시글 id 넘기기)
                intent.putExtra("path",filePath);
                intent.putExtra("name",market_upload_name.getText().toString());
                intent.putExtra("price",market_upload_price.getText().toString());
                intent.putExtra("text",market_upload_text.getText().toString());
                intent.putExtra("loginId",login_id);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
