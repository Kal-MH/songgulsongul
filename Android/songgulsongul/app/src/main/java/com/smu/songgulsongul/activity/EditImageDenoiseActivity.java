package com.smu.songgulsongul.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import com.smu.songgulsongul.songgul;
import com.smu.songgulsongul.R;

import es.dmoral.toasty.Toasty;

public class EditImageDenoiseActivity extends AppCompatActivity {

    public enum denoiseMethod {
        None, Gaussian, MedianFiltering, FastNlMeans
    }

    int BackColor = Color.parseColor("#BFB1D8");
    int FontColor = Color.parseColor("#000000");

    long first_time = 0;
    long second_time = 0;

    ImageView editPreview;

    SeekBar seekBarLuminance;
    SeekBar seekBarColor;

    LinearLayout denoiseNone;
    LinearLayout denoiseGaussian;
    LinearLayout denoiseMedianFiltering;
    LinearLayout denoiseFastNlMeans;

    Button done;

    long paperImgAddress;
    long croppedImgAddress;
    long editingImageAddress;
    //long editingImageColorAddress;

    Mat previewImage;
    //Mat editingImageResized;
    Bitmap previewImageBitmap;

    denoiseMethod selectedDenoiseMethod = denoiseMethod.MedianFiltering;

    public native void denoiseColorImage(long inputImageAddress, long outputImageAddress, int luminanceProgress, int colorProgress);

    public native void denoiseColorImageGaussian(long inputImageAddress, long outputImageAddress, int luminanceProgress, int colorProgress);

    public native void denoiseColorImageMedianFiltering(long inputImageAddress, long outputImageAddress, int luminanceProgress, int colorProgress);


    public void updatePreviewDenoise() {
        Mat locMat = new Mat();
        previewImage.release();
        updateDenoise(editingImageAddress, locMat.getNativeObjAddr());
        //updateDenoise(editingImageResized.getNativeObjAddr(),locMat.getNativeObjAddr());
        previewImage = locMat;
    }

    public void updateDenoise(long inputImageAddress, long outputImageAddress) {
        //denoiseColorImage(inputImageAddress,outputImageAddress, seekBarLuminance.getProgress(),seekBarColor.getProgress());
        //denoiseColorImageMedianFiltering(inputImageAddress,outputImageAddress, seekBarLuminance.getProgress(),seekBarColor.getProgress());
        //denoiseColorImageGaussian(inputImageAddress,outputImageAddress, seekBarLuminance.getProgress(),seekBarColor.getProgress());
        switch (selectedDenoiseMethod) {
            case None:
                break;
            case Gaussian:
                denoiseColorImageGaussian(inputImageAddress, outputImageAddress, seekBarLuminance.getProgress(), seekBarColor.getProgress());
                break;
            case MedianFiltering:
                denoiseColorImageMedianFiltering(inputImageAddress, outputImageAddress, seekBarLuminance.getProgress(), seekBarColor.getProgress());
                break;
            case FastNlMeans:
                denoiseColorImage(inputImageAddress, outputImageAddress, seekBarLuminance.getProgress(), seekBarColor.getProgress());
                break;
            default:
                break;
        }
    }

    public void updatePreviewImageView() {
        if (previewImageBitmap != null)
            previewImageBitmap.recycle();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                previewImageBitmap = Bitmap.createBitmap(previewImage.cols(), previewImage.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(previewImage, previewImageBitmap);
                editPreview.setImageBitmap(previewImageBitmap);
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image_denoise);

        seekBarLuminance = findViewById(R.id.seekBarLuminance);
        seekBarColor = findViewById(R.id.seekBarColor);


        done = findViewById(R.id.edit_done);

        editPreview = findViewById(R.id.editPreview);

        editingImageAddress = getIntent().getLongExtra("editingImageAddress", 0);
        editingImageAddress = ((songgul) getApplication()).getEditingMat().getNativeObjAddr();
        previewImage = ((songgul) getApplication()).getEditingMat().clone();
        updatePreviewDenoise();
        updatePreviewImageView();

        denoiseNone = findViewById(R.id.DenoiseNone);
        denoiseGaussian = findViewById(R.id.DenoiseGaussian);
        denoiseMedianFiltering = findViewById(R.id.DenoiseMedian);
        denoiseFastNlMeans = findViewById(R.id.DenoiseStrong);

        denoiseNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDenoiseMethod = denoiseMethod.None;
                if (previewImage != null)
                    previewImage.release();
                previewImage = ((songgul) getApplication()).getEditingMat().clone();
                updatePreviewImageView();
            }
        });

        denoiseGaussian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDenoiseMethod = denoiseMethod.Gaussian;
                updatePreviewDenoise();
                updatePreviewImageView();
            }
        });


        denoiseMedianFiltering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDenoiseMethod = denoiseMethod.MedianFiltering;
                updatePreviewDenoise();
                updatePreviewImageView();
            }
        });

        denoiseFastNlMeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDenoiseMethod = denoiseMethod.FastNlMeans;
                updatePreviewDenoise();
                updatePreviewImageView();
            }
        });


        seekBarLuminance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*
                Mat locMat = new Mat();
                previewImage.release();
                updateColors(editingImageAddress,locMat.getNativeObjAddr());
                previewImage = locMat;*/
                updatePreviewDenoise();
                updatePreviewImageView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*
                Mat locMat = new Mat();
                previewImage.release();
                updateColors(editingImageAddress,locMat.getNativeObjAddr());
                previewImage = locMat;*/
                updatePreviewDenoise();
                updatePreviewImageView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        /*
        seekBarColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePreviewDenoise();
                updatePreviewImageView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/


        //편집 적용
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent( EditActivity.this , EditDoneActivity.class);
                //startActivity(intent);
                //TODO: 네이티브 어드레스로 접근한 Mat 객체도 double free 이슈가 발생하는가?
                //Mat locMat = new Mat();
                //Mat oldEditingMat = new Mat(editingImageAddress);
                updateDenoise(editingImageAddress, editingImageAddress);

                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if (second_time - first_time < 2000) {
            super.onBackPressed();
            finish();
        } else {
            Toasty.custom(this, "한번 더 누르면 적용을 취소합니다", null, BackColor, FontColor, 2000, false, true).show();
            first_time = System.currentTimeMillis();
        }
    }


    @Override
    public void finish() {
        super.finish();
        previewImageBitmap.recycle();
        previewImage.release();
    }
}
