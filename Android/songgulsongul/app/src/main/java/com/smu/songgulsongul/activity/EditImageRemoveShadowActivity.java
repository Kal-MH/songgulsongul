package com.smu.songgulsongul.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;


import com.smu.songgulsongul.songgul;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import com.smu.songgulsongul.R;

import es.dmoral.toasty.Toasty;

public class EditImageRemoveShadowActivity extends AppCompatActivity {
    long first_time = 0;
    long second_time = 0;

    ImageView editPreview;

    Button done;

    Mat previewImage;
    Bitmap previewImageBitmap;

    Mat shadowRemovedImageCache;

    long paperImgAddress;
    long croppedImgAddress;
    long editingImageAddress;

    SeekBar seekBarAlpha;

    int BackColor = Color.parseColor("#BFB1D8");
    int FontColor = Color.parseColor("#000000");

    public native void getShadowRemovedImage(long inputAdd, long outputAdd);

    public native void lerpShadowRemovedImage(long inputAddOrigin, long inputAddShadowRemoved, long outputAdd, int progress);


    public void updatePreviewImageView() {
        /**/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (previewImageBitmap != null)
                    previewImageBitmap.recycle();
                previewImageBitmap = Bitmap.createBitmap(previewImage.cols(), previewImage.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(previewImage, previewImageBitmap);
                editPreview.setImageBitmap(previewImageBitmap);
            }
        });

        /*
        if(previewImageBitmap!=null)
            previewImageBitmap.recycle();
        previewImageBitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(previewImage, previewImageBitmap);
        editPreview.setImageBitmap(previewImageBitmap);
         */
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_shadow_remove);


        done = findViewById(R.id.edit_done);
        seekBarAlpha = findViewById(R.id.seekBarAlpha);
        editPreview = findViewById(R.id.editPreview);

        //editingImageAddress = getIntent().getLongExtra("editingImageAddress", 0);
        editingImageAddress = ((songgul) getApplication()).getEditingMat().getNativeObjAddr();
        previewImage = ((songgul) getApplication()).getEditingMat().clone();


        shadowRemovedImageCache = previewImage.clone();
        //shadowRemovedImageCache = new Mat();

        getShadowRemovedImage(editingImageAddress, shadowRemovedImageCache.getNativeObjAddr());
        lerpShadowRemovedImage(editingImageAddress, shadowRemovedImageCache.getNativeObjAddr(), previewImage.getNativeObjAddr(), seekBarAlpha.getProgress());


        updatePreviewImageView();


        seekBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lerpShadowRemovedImage(editingImageAddress, shadowRemovedImageCache.getNativeObjAddr(), previewImage.getNativeObjAddr(), progress);
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

                lerpShadowRemovedImage(editingImageAddress, shadowRemovedImageCache.getNativeObjAddr(), editingImageAddress, seekBarAlpha.getProgress());

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

        shadowRemovedImageCache.release();
    }


}
