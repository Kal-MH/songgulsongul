package com.smu.songgulsongul.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import java.util.ArrayList;

import com.smu.songgulsongul.ImageUtil;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.layout.DrawRect;
import com.smu.songgulsongul.layout.ZoomView;
import com.smu.songgulsongul.songgul;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


// 카메라 촬영및 갤러리에서 선택후 임시로 전달할 Activity
public class DetectPaperActivity extends AppCompatActivity implements View.OnTouchListener  {
    ImageView zoom_background , help;
    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }
    String filePath;
    String sourceFilePath;
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

    long first_time = 0;
    long second_time = 0;
    int th1 = 15;
    int th2 = 150;
    float scaleFactor = 1.0f;
    float originalRatio = 1.0f;
    float zoomBackgroundRatio = 1.0f;
    private int pivotOffsetX;
    private int pivotOffsetY;
    private Mat imgInput;
    private Mat imgOutput;
    private MatOfPoint paperPoints;
    Bitmap imgInputBitmap;

    private boolean findPaperOnce = false;


    public native void GetPaperPoints(long inputImage,long outputPoint, int th1, int th2);

    public native void PaperProcessing(long inputImage, long outputImage, long inputPoints, int offsetX, int offsetY, float scaleFactor ,int th1, int th2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_paper);
        //gets the file path
        filePath = getIntent().getStringExtra("path");
        sourceFilePath = filePath;
        Log.i("imread",filePath);

        //imgInput = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_COLOR);
        imgInput = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_COLOR);
        Imgproc.cvtColor(imgInput,imgInput, Imgproc.COLOR_BGR2RGB);//RGB BGR 채널 뒤밖임 수정
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

        //대상 이미지 동기 로딩
        imgInputBitmap= Bitmap.createBitmap(imgInput.cols(),imgInput.rows(), Bitmap.Config.ARGB_8888);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //이미지뷰 비트맵 설정
                Utils.matToBitmap(imgInput,imgInputBitmap);
                zoom_background.setImageBitmap(imgInputBitmap);
            }
        });
        

        //툴바 세팅
        toolbar = findViewById(R.id.detect_paper_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Step 1");
        //actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기

        //헬프버튼 세팅
        help = findViewById(R.id.detect_paper_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetectPaperActivity.this, DetectPaperHelpActivity.class);
                startActivity(intent);
            }
        });


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
                intent.putExtra("sourceFilePath", sourceFilePath);
                imgOutput = new Mat();
                PaperProcessing(imgInput.getNativeObjAddr(),imgOutput.getNativeObjAddr(),paperPoints.getNativeObjAddr(),pivotOffsetX,pivotOffsetY,scaleFactor,th1,th2);
                //Log.w("DetectPaper", "paperImage Address: "+ String.valueOf(imgOutput.getNativeObjAddr()));
                intent.putExtra("imgInputAddress", imgOutput.getNativeObjAddr());
                ((songgul)getApplication()).setPaperMat(imgOutput);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if(second_time-first_time <2000){
            super.onBackPressed();
            ((songgul)getApplication()).releaseAllMat();
            finish();
        }
        else{
            Toast.makeText(this,"한번 더 누르면 편집을 종료합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }

    ArrayList<int[]>getPos(){

        ArrayList<int[]> arrayList= new ArrayList<>();
        arrayList.add(new int[]{300, 300});
        arrayList.add(new int[]{300, 600});
        arrayList.add(new int[]{600, 300});
        arrayList.add(new int[]{600, 600});

        return arrayList;
    }

    ArrayList<int[]>get0Pos(){

        ArrayList<int[]> arrayList= new ArrayList<>();
        arrayList.add(new int[]{0, 0});
        arrayList.add(new int[]{0, 0});
        arrayList.add(new int[]{0, 0});
        arrayList.add(new int[]{0, 0});

        return arrayList;
    }


    void setDots(){

        ///opencv로 종이 찾기
        //BitmapDrawable targetDrawable = (BitmapDrawable) zoom_background.getDrawable();
        //Bitmap targetBitmap = targetDrawable.getBitmap();/*
        //Utils.bitmapToMat(targetBitmap, imgInput);


        GetPaperPoints(imgInput.getNativeObjAddr(), paperPoints.getNativeObjAddr(), th1, th2);
        boolean canUsePoints = true;
        
        //비동기 이미지 로드용 코드
        /*
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

            int maxX=0;
            int maxY=0;
            int minX=imgInput.width();
            int minY=imgInput.height();
            ///OnCreate에서 zoom_background는 아직 안그려져서 measuredHeight, height, MaxHeight 다 고장나서 나옴
            //zoom_background.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //zoom_background.getDrawable().getIntrinsicWidth()은 이미지를 비동기 로드했을때 null 문제가 발생 할 수 있어서 사용 불가->직접 이미지 사이즈를 예측계산필요
            //boolean useHeight = imgInput.width()<imgInput.height();
            ArrayList<int[]> tempPos = new ArrayList<int[]>();
            for (Point p: paperPoints.toList()) {
                if(p.x >imgInput.width() | p.y>imgInput.height()){
                    Log.w("PaperDetect", "The Point is over the imgInput! use default pos.");

                    flag = false;
                    break;
                }
                maxX = Math.max(maxX, (int)p.x);
                maxY = Math.max(maxY, (int)p.y);
                minX = Math.min(minX, (int)p.x);
                minY = Math.min(minY, (int)p.y);

                tempPos.add(new int[]{(int)(p.x/scaleFactor)+pivotOffsetX, (int)(p.y/scaleFactor)+pivotOffsetY});
                //pos.add(new int[]{0,0}); // 테스트용 0 처리
                Log.i("PaperDetect", String.valueOf((p.x/scaleFactor)) + ", " + String.valueOf((p.y/scaleFactor)));

            }
            Log.i("PaperDetect", "paperSize Percentage" + String.valueOf(scaleFactor*(maxX-minX)*(maxY-minY)/(float)(imgInput.width()*imgInput.height())));
            if(scaleFactor*(maxX-minX)*(maxY-minY)/(float)(imgInput.width()*imgInput.height())<0.5){
                Log.w("PaperDetect", "Detected paper size is too small! use default points");
                flag = false;
            }


            if(canUsePoints){
                pos = tempPos;
            }
            else{
                pos = getPos();
                ArrayList<Point> tempPoint = new ArrayList<Point>();
                for (int[] p: pos) {
                    int locX = Math.max((int)((p[0]-pivotOffsetX)*scaleFactor), 0);
                    locX = Math.min(locX, imgInput.width());
                    int locY = Math.max((int)((p[1]-pivotOffsetY)*scaleFactor), 0);
                    locY = Math.min(locY, imgInput.height());
                    tempPoint.add(new Point(locX, locY));
                }
                paperPoints.fromList(tempPoint);
            }




        }
        else{
            pos = getPos();
            Log.w("PaperDetect", "Cant find 4 points of paper! use default points");

            ArrayList<Point> tempPoint = new ArrayList<Point>();
            for (int[] p: pos) {
                int locX = Math.max((int)((p[0]-pivotOffsetX)*scaleFactor), 0);
                locX = Math.min(locX, imgInput.width());
                int locY = Math.max((int)((p[1]-pivotOffsetY)*scaleFactor), 0);
                locY = Math.min(locY, imgInput.height());
                tempPoint.add(new Point(locX, locY));
            }
            paperPoints.fromList(tempPoint);

        }
        */

        //동기 이미지로드용 코드
        ArrayList<int[]> tempPos = new ArrayList<int[]>();
        if(paperPoints.toList().size() ==4) {
            //pos.clear();


            float[] imageValues = new float[9];
            zoom_background.getImageMatrix().getValues(imageValues);
            float scaleX = imageValues[Matrix.MSCALE_X];
            float scaleY = imageValues[Matrix.MSCALE_Y];
            Drawable backgroundDrawable= zoom_background.getDrawable();
            final int origW = backgroundDrawable.getIntrinsicWidth();
            final int origH = backgroundDrawable.getIntrinsicHeight();
            final int actW = Math.round(origW * scaleX);
            final int actH = Math.round(origH * scaleY);

            int maxX = 0;
            int maxY = 0;
            int minX = imgInput.width();
            int minY = imgInput.height();
            ///OnCreate에서 zoom_background는 아직 안그려져서 measuredHeight, height, MaxHeight 다 고장나서 나옴
            //zoom_background.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //zoom_background.getDrawable().getIntrinsicWidth()은 이미지를 비동기 로드했을때 null 문제가 발생 할 수 있어서 사용 불가->직접 이미지 사이즈를 예측계산필요
            //boolean useHeight = imgInput.width()<imgInput.height();

            for (Point p : paperPoints.toList()) {
                if (p.x > imgInput.width() | p.y > imgInput.height()) {
                    Log.w("PaperDetect", "The Point is over the imgInput! use default pos.");

                    canUsePoints = false;
                    break;
                }
                maxX = Math.max(maxX, (int) p.x);
                maxY = Math.max(maxY, (int) p.y);
                minX = Math.min(minX, (int) p.x);
                minY = Math.min(minY, (int) p.y);

                int[] locReturnP = ImageUtil.ImagePointToImageView(zoom_background, (int)p.x, (int)p.y);
                tempPos.add(new int[]{locReturnP[0], locReturnP[1]});

                //tempPos.add(new int[]{(int) (p.x / scaleFactor) + pivotOffsetX, (int) (p.y / scaleFactor) + pivotOffsetY});
                //pos.add(new int[]{0,0}); // 테스트용 0 처리
                //Log.i("PaperDetect", String.valueOf((p.x / scaleFactor)) + ", " + String.valueOf((p.y / scaleFactor)));

            }
            if((maxX-minX)*(maxY-minY)/(actW*actH)<0.5){
                Log.w("PaperDetect", "Detected paper size is too small! use default points");
                canUsePoints = false;
            }
        }
        if(canUsePoints){
            pos = tempPos;
        }
        else{
            pos = getPos();
            //pos = get0Pos();
            Log.w("PaperDetect", "Cant find 4 points of paper! use default points");

            ArrayList<Point> tempPoint = new ArrayList<Point>();
            for (int[] p: pos) {
                int[] locReturnP = ImageUtil.ImagePointToImageView(zoom_background, p[0], p[1]);
                tempPos.add(new int[]{locReturnP[0], locReturnP[1]});
            }
            paperPoints.fromList(tempPoint);
        }
        
        
        dot1.setX(pos.get(0)[0]);
        dot1.setY(pos.get(0)[1]);


        dot2.setX(pos.get(1)[0]);
        dot2.setY(pos.get(1)[1]);


        dot3.setX(pos.get(2)[0]);
        dot3.setY(pos.get(2)[1]);


        dot4.setX(pos.get(3)[0]);
        dot4.setY(pos.get(3)[1]);



        Log.i("DetectPaper", "dot1 pos: " + String.valueOf(dot1.getX()) + ", "+String.valueOf(dot1.getY()));

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
        if(hasFocus){
            if(!findPaperOnce){///최초 1회 실행
                //Glide.with(this).load(filePath).into(zoom_background);//비동기 이미지 로드

                setDots();
                dots = findViewById(R.id.dot_draw);
                if(dots != null){
                    rect = new Rect(
                            0, 0,
                            dots.getMeasuredWidth(), dots.getMeasuredHeight()
                    );
                }
                findPaperOnce = true;

                drawRect = new DrawRect(this, pos,dot1.getWidth()/2);
                dots.addView(drawRect);
            }
        }

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
            /*
            if(v.getId() == dot1.getId()){
                pos.set(0, new int[]{(int) (startX + (event.getRawX()/zoom)-oldXvalue),
                        (int) (startY + ((event.getRawY() - tb_height)/zoom)-(oldYvalue + v.getHeight()/zoom))});
            }
            if(v.getId() == dot2.getId()){
                pos.set(1, new int[]{(int) (startX + (event.getRawX()/zoom)-oldXvalue),
                        (int) (startY + ((event.getRawY() - tb_height)/zoom)-(oldYvalue + v.getHeight()/zoom))});
            }
            if(v.getId() == dot3.getId()){
                pos.set(2, new int[]{(int) (startX + (event.getRawX()/zoom)-oldXvalue),
                        (int) (startY + ((event.getRawY() - tb_height)/zoom)-(oldYvalue + v.getHeight()/zoom))});
            }
            if(v.getId() == dot4.getId()){
                pos.set(3, new int[]{(int) (startX + (event.getRawX()/zoom)-oldXvalue),
                        (int) (startY + ((event.getRawY() - tb_height)/zoom)-(oldYvalue + v.getHeight()/zoom))});
            }
            */

            /*
            pos.set(0, new int[]{(int)dot1.getX(), (int)dot1.getY()});
            pos.set(1, new int[]{(int)dot2.getX(), (int)dot2.getY()});
            pos.set(2, new int[]{(int)dot3.getX(), (int)dot3.getY()});
            pos.set(3, new int[]{(int)dot4.getX(), (int)dot4.getY()});
            */
            pos.get(0)[0] = (int) dot1.getX();
            pos.get(0)[1] = (int) dot1.getY();
            pos.get(1)[0] = (int) dot2.getX();
            pos.get(1)[1] = (int) dot2.getY();
            pos.get(2)[0] = (int) dot3.getX();
            pos.get(2)[1] = (int) dot3.getY();
            pos.get(3)[0] = (int) dot4.getX();
            pos.get(3)[1] = (int) dot4.getY();

            ArrayList<Point> tempPoint = new ArrayList<Point>();
            for (int[] p: pos) {
                //비동기 이미지 로드용 코드
                /*
                int locX = Math.max((int)((p[0]-pivotOffsetX)*scaleFactor), 0);
                locX = Math.min(locX, imgInput.width());
                int locY = Math.max((int)((p[1]-pivotOffsetY)*scaleFactor), 0);
                locY = Math.min(locY, imgInput.height());
                tempPoint.add(new Point(locX, locY));
                */
                //동기 이미지 로드용 코드
                int[]loc = ImageUtil.ImageViewPointToImage(zoom_background,p[0],p[1]);
                tempPoint.add(new Point(loc[0], loc[1]));
                //Log.i("PaperDetect", String.valueOf(p[0])+", "+String.valueOf(p[0]));
            }
            Log.i("DetectPaper", "dot1 pos: " + String.valueOf(dot1.getX()) + ", "+String.valueOf(dot1.getY()));
            paperPoints.fromList(tempPoint);

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
                // 알림팝업
                return true;
            }

            case R.id.toolbar_skip:// 건너뛰기

                Intent intent = new Intent(DetectPaperActivity.this , DetectPicActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("sourceFilePath", sourceFilePath);
                imgOutput = imgInput.clone();
                intent.putExtra("imgInputAddress", imgOutput.getNativeObjAddr());
                ((songgul)getApplication()).setPaperMat(imgOutput);
                startActivity(intent);
                finish();
                return true;

        }
        return  true;
    }

    @Override
    public void finish() {

        imgInput.release();
        imgInputBitmap.recycle();
        paperPoints.release();
        super.finish();
    }
}