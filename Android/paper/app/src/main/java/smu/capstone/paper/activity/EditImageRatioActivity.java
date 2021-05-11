package smu.capstone.paper.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import smu.capstone.paper.R;

public class EditImageRatioActivity extends AppCompatActivity {

    long first_time = 0;
    long second_time = 0;

    long paperImgAddress;
    long croppedImgAddress;
    long editingImageAddress;
    long editingImageRatioAddress;

    int seekBarProgress = 50;

    Mat previewImage;

    SeekBar ratioSeekBar;
    Button done;

    ImageView editPreview;

    public native void changeImageRatio(long inputImgAddress, long outputImgAddress ,int seekBarProgress);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image_ratio);

        ratioSeekBar = findViewById(R.id.ratioSeekBar);
        done = findViewById(R.id.edit_done);
        editPreview = findViewById(R.id.editPreview);

        editingImageAddress = getIntent().getLongExtra("editingImageAddress", 0x00);
        try{
            Mat locMat = new Mat(editingImageAddress);

            //편집 취소해도 연동되지 않게 별도 객체로 분리
            //previewImage.copyTo(previewImage);
            previewImage = locMat.clone();
            //previewImage = previewImage.clone();
        }
        catch (Exception e){

        }
        if(previewImage != null){
            Bitmap loc_bitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(previewImage, loc_bitmap);
            editPreview.setImageBitmap(loc_bitmap);
        }


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent( EditActivity.this , EditDoneActivity.class);
                //startActivity(intent);

                changeImageRatio(editingImageAddress, editingImageAddress,seekBarProgress);
                finish();
            }
        });

        ratioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    seekBarProgress = progress;
                    changeImageRatio(editingImageAddress,previewImage.getNativeObjAddr(),progress);
                    Bitmap loc_bitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(previewImage, loc_bitmap);
                    editPreview.setImageBitmap(loc_bitmap);
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
            Toast.makeText(this,"한번 더 누르면 적용을 취소합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }

}
