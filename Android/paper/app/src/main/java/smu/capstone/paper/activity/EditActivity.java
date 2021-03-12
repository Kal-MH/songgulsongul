package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.layout.ZoomView;


// 카메라 촬영및 갤러리에서 선택후 임시로 전달할 Activity
public class EditActivity extends AppCompatActivity implements View.OnTouchListener  {
    ImageView zoom_background;
    String filePath;
    FrameLayout zoomFrame;


    ImageView dot1,dot2,dot3, dot4;

    float oldXvalue;
    float oldYvalue;

    ArrayList<int[]> pos;

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





        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);



        setDots();


        dot1.setOnTouchListener(this);
        dot2.setOnTouchListener(this);
        dot3.setOnTouchListener(this);
        dot4.setOnTouchListener(this);




    }

    ArrayList<int[]>getPos(){

        ArrayList<int[]> arrayList= new ArrayList<>();
        arrayList.add(new int[]{300, 300});
        arrayList.add(new int[]{300, 600});
        arrayList.add(new int[]{600, 300});
        arrayList.add(new int[]{600, 600});

        return arrayList;
    }

    void setDots(){

        pos = getPos();

        dot1.setX(pos.get(0)[0]);
        dot1.setY(pos.get(0)[1]);


        dot2.setX(pos.get(1)[0]);
        dot2.setY(pos.get(1)[1]);


        dot3.setX(pos.get(2)[0]);
        dot3.setY(pos.get(2)[1]);


        dot4.setX(pos.get(3)[0]);
        dot4.setY(pos.get(3)[1]);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //이미지 배경화면세팅
        Glide.with(this).load(filePath).into(zoom_background);



        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int width = ((ViewGroup) v.getParent()).getWidth() - v.getWidth();
        int height = ((ViewGroup) v.getParent()).getHeight() - v.getHeight();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            oldXvalue = event.getX();
            oldYvalue = event.getY();
            Log.d("TAG", "Action Down rX " + event.getRawX() + "," + event.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            v.setX(event.getRawX() - oldXvalue);
            v.setY(event.getRawY() - (oldYvalue + v.getHeight()));
            //  Log.i("Tag2", "Action Down " + me.getRawX() + "," + me.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (v.getX() > width && v.getY() > height) {
                v.setX(width);
                v.setY(height);
            } else if (v.getX() < 0 && v.getY() > height) {
                v.setX(0);
                v.setY(height);
            } else if (v.getX() > width && v.getY() < 0) {
                v.setX(width);
                v.setY(0);
            } else if (v.getX() < 0 && v.getY() < 0) {
                v.setX(0);
                v.setY(0);
            } else if (v.getX() < 0 || v.getX() > width) {
                if (v.getX() < 0) {
                    v.setX(0);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                } else {
                    v.setX(width);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                }
            } else if (v.getY() < 0 || v.getY() > height) {
                if (v.getY() < 0) {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(0);
                } else {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(height);
                }
            }


        }
        return true;
    }
}