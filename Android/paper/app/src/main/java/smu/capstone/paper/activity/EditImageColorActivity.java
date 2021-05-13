package smu.capstone.paper.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import smu.capstone.paper.R;

public class EditImageColorActivity extends AppCompatActivity {

    long first_time = 0;
    long second_time = 0;

    ImageView editPreview;

    SeekBar seekBarHue;
    SeekBar seekBarSaturation;
    SeekBar seekBarBrightness;
    SeekBar seekBarContrast;

    Button done;

    long paperImgAddress;
    long croppedImgAddress;
    long editingImageAddress;
    //long editingImageColorAddress;

    Mat previewImage;
    Bitmap previewImageBitmap;


    public native void setColors(long inputImageAddress, long outputImageAddress, int hueProgress, int saturationProgress, int brightnessProgress, int contrastProgress);

    public void updateColors(long inputImageAddress, long outputImageAddress){
        setColors(inputImageAddress,outputImageAddress, seekBarHue.getProgress(),seekBarSaturation.getProgress(),seekBarBrightness.getProgress(),seekBarContrast.getProgress());



    }

    public void updatePreviewImageView(){
        if(previewImageBitmap!=null)
            previewImageBitmap.recycle();
        previewImageBitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(previewImage, previewImageBitmap);
        editPreview.setImageBitmap(previewImageBitmap);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image_colors);

        seekBarHue = findViewById(R.id.seekBarHue);
        seekBarSaturation = findViewById(R.id.seekBarSaturation);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);
        seekBarContrast = findViewById(R.id.seekBarContrast);

        done = findViewById(R.id.edit_done);

        editPreview = findViewById(R.id.editPreview);

        editingImageAddress = getIntent().getLongExtra("editingImageAddress", 0);
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
            /*
            Bitmap loc_bitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(previewImage, loc_bitmap);
            editPreview.setImageBitmap(loc_bitmap);
            */
            updatePreviewImageView();
        }



        seekBarHue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColors(editingImageAddress,previewImage.getNativeObjAddr());
                updatePreviewImageView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColors(editingImageAddress,previewImage.getNativeObjAddr());
                updatePreviewImageView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColors(editingImageAddress,previewImage.getNativeObjAddr());
                updatePreviewImageView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColors(editingImageAddress,previewImage.getNativeObjAddr());
                updatePreviewImageView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //편집 적용
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Intent intent = new Intent( EditActivity.this , EditDoneActivity.class);
            //startActivity(intent);
            updateColors(editingImageAddress,editingImageAddress);
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
