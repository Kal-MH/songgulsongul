package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.layout.DrawView;
import smu.capstone.paper.layout.ZoomView;


// 카메라 촬영및 갤러리에서 선택후 임시로 전달할 Activity
public class EditActivity extends AppCompatActivity {
    ImageView zoom_background;
    String filePath;
    FrameLayout zoomFrame;
    FrameLayout drawFrame;


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



        drawFrame = findViewById(R.id.canvas);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //이미지 배경화면세팅
        Glide.with(this).load(filePath).into(zoom_background);

        Rect rect = new Rect(
                0, 0,
                drawFrame.getMeasuredWidth(), drawFrame.getMeasuredHeight()
        );

        ArrayList<int[]> arrayList= new ArrayList<>();
        arrayList.add(new int[]{300, 300});
        arrayList.add(new int[]{300, 600});
        arrayList.add(new int[]{600, 300});
        arrayList.add(new int[]{600, 600});

        DrawView drawView = new DrawView(this,rect,arrayList);
        drawFrame.addView(drawView);


        super.onWindowFocusChanged(hasFocus);
    }
}