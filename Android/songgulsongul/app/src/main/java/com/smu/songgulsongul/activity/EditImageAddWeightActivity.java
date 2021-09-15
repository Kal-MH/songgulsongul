package com.smu.songgulsongul.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


import com.smu.songgulsongul.songgul;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import com.smu.songgulsongul.R;

public class EditImageAddWeightActivity extends AppCompatActivity {
    long first_time = 0;
    long second_time = 0;

    ImageView editPreview;

    Button done;

    Mat previewImage;
    Bitmap previewImageBitmap;


    long paperImgAddress;
    long croppedImgAddress;
    long editingImageAddress;


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

        setContentView(R.layout.activity_edit_image_add_weight);


        done = findViewById(R.id.edit_done);

        editPreview = findViewById(R.id.editPreview);

        //editingImageAddress = getIntent().getLongExtra("editingImageAddress", 0);
        editingImageAddress = ((songgul) getApplication()).getEditingMat().getNativeObjAddr();
        previewImage = ((songgul) getApplication()).getEditingMat().clone();
        updatePreviewImageView();


        //편집 적용
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}
