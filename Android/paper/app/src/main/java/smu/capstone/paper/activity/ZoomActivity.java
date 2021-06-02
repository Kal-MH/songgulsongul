  package smu.capstone.paper.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import smu.capstone.paper.R;
import smu.capstone.paper.layout.ZoomView;

  public class ZoomActivity extends AppCompatActivity {

     String path;
     ZoomView zoomView;
     FrameLayout zoomFrame;
     ImageView zoomImage;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);


        //툴바 세팅
        Toolbar toolbar = (Toolbar)findViewById(R.id.zoom_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Zoom");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        zoomFrame = findViewById(R.id.zoomframe);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        if (setZoom())
           Glide.with(this).load(path).into(zoomImage); // 게시물 사진
     }



     public boolean setZoom(){

        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = inflater.inflate(R.layout.zoom_img, null, false);
        view.setLayoutParams(layoutParams);
        zoomImage = view.findViewById(R.id.zoom_item);

        //zoom View 세팅
        zoomView = new ZoomView((Context)this);
        zoomView.addView(view);
        zoomView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        zoomView.setMaxZoom(4.0F);

        zoomFrame.addView(zoomView);

        return true;
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