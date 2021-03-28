package smu.capstone.paper.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import smu.capstone.paper.R;

public class DetectPicActivity extends AppCompatActivity {

    String filePath;
    ImageView detect_pic_imageView;
    Toolbar toolbar;
    CropImageView cropImageView;
    Button okbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_pic);

       // detect_pic_imageView = findViewById(R.id.detect_pic_iv);
        filePath = getIntent().getStringExtra("path");
        Uri imageUri = Uri.fromFile(new File(filePath));

        //툴바 세팅
        toolbar = findViewById(R.id.detect_pic_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기



        cropImageView = (CropImageView) findViewById(R.id.cropImageView);

        // 가져온 이미지 세팅
        cropImageView.setImageUriAsync(imageUri);

        //세부내용 세팅
        cropImageView.setGuidelines(CropImageView.Guidelines.ON);
        cropImageView.setScaleType(CropImageView.ScaleType.FIT_CENTER);
        cropImageView.setAutoZoomEnabled(true);
        cropImageView.setCropRect(new Rect(400, 400, 800, 500));



        okbtn = findViewById(R.id.detect_pic_btn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap cropped = cropImageView.getCroppedImage();
                File temp = (File)saveBitmapToCache(cropped,"crop_temp");
                String filePath= temp.getAbsolutePath();
                Intent intent = new Intent(DetectPicActivity.this, EditActivity.class);
                intent.putExtra("path", filePath);
                startActivity(intent);
            }
        });
    }

    private Object saveBitmapToCache(Bitmap bitmap, String name) {

        File tempFile = null;
        try {
            tempFile = File.createTempFile(name, null, getCacheDir());

            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            return tempFile;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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