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

import com.smu.songgulsongul.songgul;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import com.smu.songgulsongul.R;

import es.dmoral.toasty.Toasty;

public class EditImageHistogramActivity extends AppCompatActivity {

    public enum editHistogramMod {
        None, Default, CLAHE
    }

    int BackColor = Color.parseColor("#BFB1D8");
    int FontColor = Color.parseColor("#000000");

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

    SeekBar alpha;

    editHistogramMod selectedHistogramMod = editHistogramMod.None;

    // 기본히스토그램 평활화
    public native void equalizeHistogram(long inputImgAddress, long outputImgAddress, int progress);

    // CLAHE(구역별) 히스토그램 평활화
    public native void equalizeHistogramClahe(long inputImgAddress, long outputImgAddress, int progress);

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

    public void updatePreviewImageView(final Mat mat) {
        if (previewImageBitmap != null)
            previewImageBitmap.recycle();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                previewImageBitmap = Bitmap.createBitmap(previewImage.cols(), previewImage.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mat, previewImageBitmap);
                editPreview.setImageBitmap(previewImageBitmap);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image_histogram);

        done = findViewById(R.id.edit_done);

        editPreview = findViewById(R.id.editPreview);

        histogramNone = findViewById(R.id.histogramNone);
        histogramDefault = findViewById(R.id.histogramDefault);
        histogramCLAHE = findViewById(R.id.histogramCLAHE);

        alpha = findViewById(R.id.seekBarAlpha);


        editingImageAddress = getIntent().getLongExtra("editingImageAddress", 0);
        editingImageAddress = ((songgul) getApplication()).getEditingMat().getNativeObjAddr();
        previewImage = ((songgul) getApplication()).getEditingMat().clone();
        updatePreviewImageView();


        //히스토그램평활화 없음 적용
        histogramNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                selectedHistogramMod = editHistogramMod.None;
                Mat locMat = new  Mat(editingImageAddress).clone(); // TODO: 얕은복사(new Mat(address))에서 메모리 누수 나는지 체크 필요
                if(previewImageBitmap!=null)
                    previewImageBitmap.recycle();
                previewImageBitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(locMat,previewImageBitmap);
                editPreview.setImageBitmap(previewImageBitmap);
                locMat.release();*/
                selectedHistogramMod = editHistogramMod.None;
                previewImage.release();
                previewImage = new Mat(editingImageAddress).clone();
                updatePreviewImageView();
            }
        });
        //히스토그램평활화 기본 적용
        histogramDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHistogramMod = editHistogramMod.Default;
                /*
                Mat locMat = new  Mat(editingImageAddress).clone(); // TODO: 얕은복사(new Mat(address))에서 메모리 누수 나는지 체크 필요
                Mat locMat2 = new Mat();
                previewImage.release();
                equalizeHistogram(locMat.getNativeObjAddr(),locMat2.getNativeObjAddr());
                previewImage = locMat2;
                if(previewImageBitmap!=null)
                    previewImageBitmap.recycle();
                previewImageBitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(previewImage,previewImageBitmap);
                editPreview.setImageBitmap(previewImageBitmap);
                locMat.release();*/
                previewImage.release();
                previewImage = new Mat(editingImageAddress).clone();
                equalizeHistogram(previewImage.getNativeObjAddr(), previewImage.getNativeObjAddr(), alpha.getProgress());
                updatePreviewImageView();
            }
        });
        //히스토그램평활화 적응형 (부분구역별) 적용
        histogramCLAHE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHistogramMod = editHistogramMod.CLAHE;
                /*
                Mat locMat = new  Mat(editingImageAddress).clone(); // TODO: 얕은복사(new Mat(address))에서 메모리 누수 나는지 체크 필요
                Mat locMat2 = new Mat();
                previewImage.release();
                equalizeHistogramClahe(locMat.getNativeObjAddr(),locMat2.getNativeObjAddr());
                previewImage = locMat2;
                if(previewImageBitmap!=null)
                    previewImageBitmap.recycle();
                previewImageBitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(previewImage,previewImageBitmap);
                editPreview.setImageBitmap(previewImageBitmap);
                locMat.release();*/
                selectedHistogramMod = editHistogramMod.CLAHE;
                previewImage.release();
                previewImage = new Mat(editingImageAddress).clone();
                equalizeHistogramClahe(previewImage.getNativeObjAddr(), previewImage.getNativeObjAddr(), alpha.getProgress());
                updatePreviewImageView();
            }
        });

        alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                previewImage.release();
                previewImage = new Mat(editingImageAddress).clone();
                switch (selectedHistogramMod) {
                    case None: //do nothing
                        break;
                    case Default:
                        equalizeHistogram(previewImage.getNativeObjAddr(), previewImage.getNativeObjAddr(), progress);
                        break;
                    case CLAHE:
                        equalizeHistogramClahe(previewImage.getNativeObjAddr(), previewImage.getNativeObjAddr(), progress);
                        break;
                    default:
                        break;
                }
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
                //TODO: 프리뷰 Mat을 그대로 옮길지 아니면 프리뷰 연산 크기를 줄이고 별도로 둘지 선택할것
                switch (selectedHistogramMod) {
                    case None: //do nothing
                        break;
                    case Default:
                        equalizeHistogram(editingImageAddress, editingImageAddress, alpha.getProgress());
                        break;
                    case CLAHE:
                        equalizeHistogramClahe(editingImageAddress, editingImageAddress, alpha.getProgress());
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
