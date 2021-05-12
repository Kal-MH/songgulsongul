package smu.capstone.paper.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.core.Mat;

import smu.capstone.paper.R;

public class EditImageHistogramActivity extends AppCompatActivity {

    long first_time = 0;
    long second_time = 0;

    ImageView editPreview;
    Button done;

    LinearLayout histogramNone;
    LinearLayout histogramDefault;
    LinearLayout histogramCLAHE;

    long paperImgAddress;
    long croppedImgAddress;
    long editingImageAddress;
    //long editingImageColorAddress;

    Mat previewImage;
    Bitmap previewImageBitmap;

    // 기본히스토그램 평활화
    public native void equalizeHistogram(long inputImgAddress, long outputImgAddress);
    // CLAHE(구역별) 히스토그램 평활화
    public native void equalizeHistogramClahe(long inputImgAddress, long outputImgAddress);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image_histogram);

        done = findViewById(R.id.edit_done);

        editPreview = findViewById(R.id.editPreview);

        histogramNone = findViewById(R.id.histogramNone);
        histogramDefault = findViewById(R.id.histogramDefault);
        histogramCLAHE = findViewById(R.id.histogramCLAHE);


        //히스토그램평활화 없음 적용
        histogramNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //히스토그램평활화 기본 적용
        histogramDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //히스토그램평활화 적응형 (부분구역별) 적용
        histogramCLAHE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //편집 적용
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
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
            Toast.makeText(this,"한번 더 누르면 적용을 취소합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }
}
