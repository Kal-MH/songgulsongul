package com.smu.songgulsongul.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.smu.songgulsongul.R;

public class MarketUploadActivity extends AppCompatActivity {
    String filePath;
    ImageView market_upload_img;
    EditText market_upload_name, market_upload_price, market_upload_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_upload);

        market_upload_name = findViewById(R.id.market_upload_name);
        market_upload_price = findViewById(R.id.market_upload_price);
        market_upload_text = findViewById(R.id.market_upload_text);

        market_upload_img = findViewById(R.id.market_upload_img);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_toolbar, menu);
        return true;
    }
}
