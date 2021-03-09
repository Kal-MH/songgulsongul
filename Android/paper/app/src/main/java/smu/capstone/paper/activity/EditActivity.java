package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import smu.capstone.paper.R;
import smu.capstone.paper.layout.ZoomView;


// 카메라 촬영및 갤러리에서 선택후 임시로 전달할 Activity
public class EditActivity extends AppCompatActivity {
    ImageView zoom_background;
    String filePath;
    FrameLayout zoomFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //gets the file path
       filePath = getIntent().getStringExtra("path");

        //zoom View 세팅
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.zoom_item, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ZoomView zoomView = new ZoomView((Context)this);
        zoomView.addView(view);
        zoomView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        zoomView.setMaxZoom(4.0F);
        zoomFrame = findViewById(R.id.edit_zoom) ;
        zoomFrame.addView(zoomView);
        zoom_background = findViewById(R.id.zoom_background);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //이미지 배경화면세팅
        Glide.with(this).load(filePath).into(zoom_background);
        super.onWindowFocusChanged(hasFocus);
    }
}