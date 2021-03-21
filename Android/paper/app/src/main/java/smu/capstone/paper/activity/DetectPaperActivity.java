package smu.capstone.paper.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.layout.DrawRect;
import smu.capstone.paper.layout.ZoomView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;


// 카메라 촬영및 갤러리에서 선택후 임시로 전달할 Activity
public class DetectPaperActivity extends AppCompatActivity implements View.OnTouchListener  {
    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    ImageView zoom_background;
    String filePath;
    FrameLayout zoomFrame , dots ;
    ZoomView zoomView;
    Toolbar toolbar;
    ImageView dot1,dot2,dot3, dot4 ;
    Rect rect;
    DrawRect drawRect;
    float oldXvalue;
    float oldYvalue;
    Button doneBtn;
    ConstraintLayout bottom;
    ArrayList<int[]> pos;


    float originalRatio = 1.0f;
    float zoomBackgroundRatio = 1.0f;
    private int pivotOffsetX;
    private int pivotOffsetY;
    private Mat imgInput;
    private Mat imgOutput;
    private MatOfPoint paperPoints;




    public native void GetPaperPoints(long inputImage,long outputPoint, int th1, int th2);

    public native void PaperProcessing(long inputImage, long outputImage, long inputPoints, int th1, int th2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_paper);

        //gets the file path
       filePath = getIntent().getStringExtra("path");
        imgInput = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_COLOR);
        paperPoints = new MatOfPoint();

        //zoom View 세팅
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.zoom_item, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        zoomView = new ZoomView((Context)this);
        zoomView.addView(view);
        zoomView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        zoomView.setMaxZoom(4.0F);

        zoomFrame = findViewById(R.id.detect_paper_frame) ;
        zoomFrame.addView(zoomView);
        zoom_background = findViewById(R.id.zoom_background);

        //툴바 세팅
        toolbar = findViewById(R.id.detect_paper_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기

        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);

        //setDots(); /// OnCreate에서 ImageView의 크기를 가져올 수 없어서 스케일 조절이 되지 않음.

        




        dot1.setOnTouchListener(this);
        dot2.setOnTouchListener(this);
        dot3.setOnTouchListener(this);
        dot4.setOnTouchListener(this);

        bottom = findViewById(R.id.detect_paper_layout);
        doneBtn = findViewById(R.id.detect_paper_next);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetectPaperActivity.this , DetectPicActivity.class);
                // 이미지 편집 후 임시파일로 저장, 인텐트에는 url로 전달
                intent.putExtra("path", filePath);
                startActivity(intent);
            }
        });

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

            ///opencv로 종이 찾기
            //BitmapDrawable targetDrawable = (BitmapDrawable) zoom_background.getDrawable();
            //Bitmap targetBitmap = targetDrawable.getBitmap();/*
            //Utils.bitmapToMat(targetBitmap, imgInput);

        float scaleFactor = 1.0f;
        GetPaperPoints(imgInput.getNativeObjAddr(), paperPoints.getNativeObjAddr(), 15, 150);
        originalRatio = imgInput.height()/(float)imgInput.width();
        zoomBackgroundRatio = zoom_background.getMeasuredHeight()/(float)zoom_background.getMeasuredWidth();

        if(originalRatio > zoomBackgroundRatio){
            scaleFactor = imgInput.height()/(float)zoom_background.getMeasuredHeight();
            pivotOffsetX = (int) (imgInput.height()/scaleFactor - zoom_background.getMeasuredHeight())/4;
            pivotOffsetY = 0;
        }
        else{
            scaleFactor = imgInput.width()/(float)zoom_background.getMeasuredWidth();
            pivotOffsetY = (int) (imgInput.height()/scaleFactor - zoom_background.getMeasuredWidth())/4;
            pivotOffsetX = 0;
        }
        Log.i("PaperDetect", "image Scale factor: "+ String.valueOf(scaleFactor));
        Log.i("PaperDetect", "pivotOffsetX: "+ String.valueOf(pivotOffsetX));
        Log.i("PaperDetect", "pivotOffsetY: "+ String.valueOf(pivotOffsetY));
        if(paperPoints.toList().size() ==4){
            //pos.clear();

            ///OnCreate에서 zoom_background는 아직 안그려져서 measuredHeight, height, MaxHeight 다 고장나서 나옴
            //zoom_background.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //zoom_background.getDrawable().getIntrinsicWidth()은 이미지를 비동기 로드했을때 null 문제가 발생 할 수 있어서 사용 불가->직접 이미지 사이즈를 예측계산필요
            //boolean useHeight = imgInput.width()<imgInput.height();
            pos = new ArrayList<int[]>();
            for (Point p: paperPoints.toList()) {
                pos.add(new int[]{(int)(p.x/scaleFactor)+pivotOffsetX, (int)(p.y/scaleFactor)+pivotOffsetY});
                //pos.add(new int[]{0,0}); // 테스트용 0 처리
                Log.i("PaperDetect", String.valueOf((p.x/scaleFactor)) + ", " + String.valueOf((p.y/scaleFactor)));
            }

        }
        else/**/
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

    void draw(){
        pos.get(0)[0] = (int) dot1.getX();
        pos.get(0)[1] = (int) dot1.getY();

        pos.get(1)[0] = (int) dot2.getX();
        pos.get(1)[1] = (int) dot2.getY();

        pos.get(2)[0] = (int) dot3.getX();
        pos.get(2)[1] = (int) dot3.getY();

        pos.get(3)[0] = (int) dot4.getX();
        pos.get(3)[1] = (int) dot4.getY();


        dots.removeAllViews();
        if(dots != null){
            rect = new Rect(
                    0, 0,
                    dots.getMeasuredWidth(), dots.getMeasuredHeight()
            );


        }
        else
            dots = findViewById(R.id.dot_draw);

        drawRect = new DrawRect(this, pos,dot1.getWidth()/2);
        dots.addView(drawRect);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //이미지 배경화면세팅
        Glide.with(this).load(filePath).into(zoom_background);//비동기 이미지 로드
        setDots();///TODO: 최초 한번만 실행되게 수정할 것
        dots = findViewById(R.id.dot_draw);
        if(dots != null){
            rect = new Rect(
                    0, 0,
                    dots.getMeasuredWidth(), dots.getMeasuredHeight()
            );
        }


        drawRect = new DrawRect(this, pos,dot1.getWidth()/2);
        dots.addView(drawRect);

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int width = ((ViewGroup) v.getParent()).getWidth() ;
        int height = ((ViewGroup) v.getParent()).getHeight();
        int tb_height = toolbar.getHeight();
        int dot_size = dot1.getWidth()/2 ;
        int zero = -1 * dot_size;

        float zoom = zoomView.getZoom();

        float startX = zoomView.getSmoothZoomX() - width/zoom*0.5f ;
        float startY =zoomView.getSmoothZoomY() - height/zoom*0.5f;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            zoomView.setDotmove(true);
            oldXvalue = event.getX();
            oldYvalue = event.getY();

        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            v.setX( startX + (event.getRawX()/zoom) - oldXvalue);
            v.setY( startY + ((event.getRawY()-tb_height)/zoom) - (oldYvalue + v.getHeight()/zoom )   );

        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {

            zoomView.setDotmove(false);
            if (v.getX() > width + dot_size && v.getY() > height + dot_size) {
                v.setX(width - dot_size);
                v.setY(height - dot_size);
                Log.d("out", "case 1");
            }
            else if (v.getX() < zero && v.getY() > height + dot_size) {
                v.setX(zero);
                v.setY(height-dot_size);
                Log.d("out", "case 2");
            }
            else if (v.getX() > width + dot_size && v.getY() < zero) {
                v.setX(width-dot_size);
                v.setY(-1 * dot_size);
                Log.d("out", "case 3");
            }
            else if (v.getX() < zero && v.getY() < zero) {
                v.setX(zero);
                v.setY(zero);
                Log.d("out", "case 4");
            }
            else if (v.getX() < zero || v.getX() > width + dot_size) {
                if (v.getX() < zero ) {
                    v.setX( zero );
                    v.setY( startY + ((event.getRawY()-tb_height)/zoom) - (oldYvalue + v.getHeight()/zoom )   );
                    Log.d("out", "case 5");
                } else {
                    v.setX( width-dot_size );
                    v.setY( startY + ((event.getRawY()-tb_height)/zoom) - (oldYvalue + v.getHeight()/zoom )   );
                    Log.d("out", "case 6");
                }
            }
            else if (v.getY() < zero || v.getY() > height + dot_size) {
                if (v.getY() < zero ) {
                    v.setX( startX + (event.getRawX()/zoom) - oldXvalue);
                    v.setY( zero );
                    Log.d("out", "case 7");
                }
                else {
                    v.setX( startX + (event.getRawX()/zoom) - oldXvalue);
                    v.setY(height- dot_size );
                    Log.d("out", "case 8");
                }
            }
        }
        draw();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.skip_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }

            case R.id.toolbar_skip:// 건너뛰기


                return true;

        }
        return  true;
    }


}