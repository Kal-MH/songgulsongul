package smu.capstone.paper.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import smu.capstone.paper.ImageUtil;
import smu.capstone.paper.R;

public class EditActivity extends AppCompatActivity {

    String filePath;
    String sourceFilePath;
    ImageView edit_iv;
    Button done;
    Toolbar toolbar;


    LinearLayout editRatio;
    LinearLayout editColors;
    LinearLayout editHistogram;
    LinearLayout editDenoise;
    LinearLayout editFilter;
    LinearLayout editTransparency;

    long first_time = 0;
    long second_time = 0;


    long croppedImageAddress;
    long paperImageAddress;

    Mat editingImage;
    Bitmap editingImageBitmap;

    public void setImageViewFromMat(){

        if(editingImage != null){
            if(editingImageBitmap !=null)
                editingImageBitmap.recycle();

            //최대 픽셀 제한
            ImageUtil.maxSize2048(editingImage.getNativeObjAddr(),editingImage.getNativeObjAddr());
            //ImageUtil.maxSizeCustom(editingImage.getNativeObjAddr(),editingImage.getNativeObjAddr(),1024);

            Log.i("EditImageSize",String.valueOf(editingImage.rows())+", " + String.valueOf(editingImage.cols()));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    editingImageBitmap = Bitmap.createBitmap(editingImage.cols(),editingImage.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(editingImage,editingImageBitmap);
                    edit_iv.setImageBitmap(editingImageBitmap);
                }
            });


        }
        else{
            // TODO: Mat 채우기

        }
    }


    private Object saveBitmapToCache(Bitmap bitmap, String name) {

        File tempFile = null;
        try {
            tempFile = File.createTempFile(name, null, getCacheDir());

            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            //bitmap.compress(Bitmap.CompressFormat.PNG,90,out);
            out.close();
            return tempFile;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        done = findViewById(R.id.edit_done);


        //툴바 세팅
        toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Step 3");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기


        editRatio = findViewById(R.id.edit_image_ratio);
        editColors = findViewById(R.id.edit_image_colors);
        editFilter = findViewById(R.id.edit_image_filter);
        editHistogram = findViewById(R.id.edit_image_histogram);
        editDenoise = findViewById(R.id.edit_image_denoise);

        filePath = getIntent().getStringExtra("path");
        sourceFilePath = getIntent().getStringExtra("sourceFilePath");
        edit_iv = findViewById(R.id.edit_pic);
        //Glide.with(this).load(filePath).into(edit_iv);


        paperImageAddress = getIntent().getLongExtra("paperImageAddress",0);
        croppedImageAddress = getIntent().getLongExtra("croppedImageAddress", 0x00);
        editingImage = new Mat(croppedImageAddress).clone();

        setImageViewFromMat();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap edited = Bitmap.createBitmap(editingImage.cols(),editingImage.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(editingImage,edited);
                File temp = (File)saveBitmapToCache(edited,"edit_temp");
                String filePath= temp.getAbsolutePath();
                Intent intent = new Intent( EditActivity.this , EditDoneActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("editedImageAddress",editingImage.getNativeObjAddr());
                startActivity(intent);

                //Edit Done 할시 되돌아 갈 수 없음! 이전 작업 정리해도 안전!
                //메모리 정리하기
                try{
                    Mat loc = new Mat(croppedImageAddress);
                    loc.release();
                }
                catch (Exception e){

                }
                try{
                    Mat loc2 = new Mat(paperImageAddress);
                    loc2.release();
                }
                catch (Exception e){

                }



                finish();
            }
        });


        editRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Bitmap edited = Bitmap.createBitmap(editingImage.cols(),editingImage.rows(), Bitmap.Config.ARGB_8888);
                //Utils.matToBitmap(editingImage,edited);
                //File temp = (File)saveBitmapToCache(edited,"edit_temp");
                //String filePath= temp.getAbsolutePath();
                Intent intent = new Intent( EditActivity.this , EditImageRatioActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("editingImageAddress",editingImage.getNativeObjAddr());
                startActivity(intent);
                //finish();//test
            }
        });

        editColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent( EditActivity.this , EditImageColorActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("editingImageAddress",editingImage.getNativeObjAddr());
                startActivity(intent);
            }
        });


        editHistogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent( EditActivity.this , EditImageHistogramActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("editingImageAddress",editingImage.getNativeObjAddr());
                startActivity(intent);
            }
        });

        editFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent( EditActivity.this , EditImageFilterActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("editingImageAddress",editingImage.getNativeObjAddr());
                startActivity(intent);
            }
        });
        editDenoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent( EditActivity.this , EditImageDenoiseActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("editingImageAddress",editingImage.getNativeObjAddr());
                startActivity(intent);
            }
        });




    }

    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if(second_time-first_time <2000){
            super.onBackPressed();
            finish();
        }
        else{
            Toast.makeText(this,"한번 더 누르면 편집을 종료합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.undo_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                // 알림팝업

                return true;


            case R.id.toolbar_before:{
                Intent intent = new Intent(EditActivity.this, DetectPicActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("imgInputAddress", paperImageAddress);
                intent.putExtra("sourceFilePath", sourceFilePath);
                new Mat(croppedImageAddress).release();
                startActivity(intent);
                finish();
                return true;
            }

            case R.id.toolbar_undo : // 원본으로 복구

                editingImage.release();
                editingImage = new Mat(croppedImageAddress).clone();
                setImageViewFromMat();


                return true;




        }
        return  true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            setImageViewFromMat();
        }


    }



    @Override
    public void finish() {
        //editingImage.release();

        new Mat(paperImageAddress).release();
        new Mat(croppedImageAddress).release();
        editingImageBitmap.recycle();
        super.finish();
    }
}