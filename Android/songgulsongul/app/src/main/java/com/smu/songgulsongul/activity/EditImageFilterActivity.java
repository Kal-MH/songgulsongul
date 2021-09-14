package com.smu.songgulsongul.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import com.smu.songgulsongul.songgul;
import com.smu.songgulsongul.R;

import es.dmoral.toasty.Toasty;

public class EditImageFilterActivity extends AppCompatActivity {

    public enum editFilter {
        None, Gray, Sharp, Test
    }

    int BackColor = Color.parseColor("#BFB1D8");
    int FontColor = Color.parseColor("#000000");

    long first_time = 0;
    long second_time = 0;

    ImageView editPreview;

    long paperImgAddress;
    long croppedImgAddress;
    long editingImageAddress;

    Button done;

    LinearLayout filterNone;
    LinearLayout filterGrey;
    LinearLayout filterSharp;
    LinearLayout filterTest;

    Mat previewImage;
    Bitmap previewImageBitmap;

    editFilter selectedFilter = editFilter.None;

    public native void applyRGBMinGray(long imgInputAddress, long imgOutputAddress);

    public native void applyTestFilter(long imgInputAddress, long imgOutputAddress);

    public native void applyFilterSharp(long imgInputAddress, long imgOutputAddress);


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

        setContentView(R.layout.activity_edit_image_filter);

        done = findViewById(R.id.edit_done);

        editPreview = findViewById(R.id.editPreview);

        filterNone = findViewById(R.id.image_edit_filter_none);
        filterGrey = findViewById(R.id.image_edit_filter_gray);
        filterSharp = findViewById(R.id.image_edit_filter_sharp);
        //filterTest = findViewById(R.id.image_edit_filter_test);

        editingImageAddress = getIntent().getLongExtra("editingImageAddress", 0);
        editingImageAddress = ((songgul) getApplication()).getEditingMat().getNativeObjAddr();
        previewImage = ((songgul) getApplication()).getEditingMat().clone();
        updatePreviewImageView();


        filterNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = editFilter.None;
                previewImage.release();
                previewImage = new Mat(editingImageAddress).clone();
                updatePreviewImageView();
            }
        });

        filterGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = editFilter.Gray;
                previewImage.release();
                previewImage = new Mat(editingImageAddress).clone();
                applyRGBMinGray(editingImageAddress, previewImage.getNativeObjAddr());
                updatePreviewImageView();
            }
        });

        filterSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = editFilter.Sharp;
                previewImage.release();
                previewImage = new Mat(editingImageAddress).clone();
                applyFilterSharp(editingImageAddress, previewImage.getNativeObjAddr());
                updatePreviewImageView();
            }
        });

        /*
        filterTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:필터 이미지 블랜딩 추가
                selectedFilter = editFilter.None;
                previewImage.release();
                previewImage = new Mat(editingImageAddress).clone();
                updatePreviewImageView();
            }
        });*/


        //편집 적용
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 프리뷰 Mat을 그대로 옮길지 아니면 프리뷰 연산 크기를 줄이고 별도로 둘지 선택할것
                switch (selectedFilter) {
                    case None: //do nothing
                        break;
                    case Gray:
                        applyRGBMinGray(editingImageAddress, editingImageAddress);
                        break;
                    case Sharp:
                        applyFilterSharp(editingImageAddress, editingImageAddress);
                        break;
                    case Test: //TODO
                        break;
                    default:
                        break;
                }
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
