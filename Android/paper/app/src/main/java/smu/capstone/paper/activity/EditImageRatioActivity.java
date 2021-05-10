package smu.capstone.paper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.core.Mat;

import smu.capstone.paper.R;

public class EditImageRatioActivity extends AppCompatActivity {

    long first_time = 0;
    long second_time = 0;

    long originalImgAddress;

    Mat previewImage;

    SeekBar ratioSeekBar;
    Button done;

    public native void changeImageRatio(long inputImgAddress, long outputImgAddress ,int seekBarProgress);


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image_ratio);

        ratioSeekBar = findViewById(R.id.ratioSeekBar);
        done = findViewById(R.id.edit_done);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent( EditActivity.this , EditDoneActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        ratioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    changeImageRatio(originalImgAddress,previewImage.getNativeObjAddr(),progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
            Toast.makeText(this,"한번 더 누르면 적용을 종료합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }

}
