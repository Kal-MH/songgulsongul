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

public class EditImageFilterActivity extends AppCompatActivity {


    long first_time = 0;
    long second_time = 0;
    ImageView editPreview;

    long paperImgAddress;
    long croppedImgAddress;
    long editingImageAddress;

    Button done;

    LinearLayout filterNone;
    LinearLayout filterGrey;
    LinearLayout filterTest;

    Mat previewImage;
    Bitmap previewImageBitmap;


    public native void applyRGBMinGrey(long imgInputAddress, long imgOutputAddress);

    public native void applyTestFilter(long imgInputAddress, long imgOutputAddress);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image_filter);

        done = findViewById(R.id.edit_done);

        editPreview = findViewById(R.id.editPreview);






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
