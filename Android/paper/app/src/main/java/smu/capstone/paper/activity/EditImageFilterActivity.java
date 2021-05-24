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

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import smu.capstone.paper.R;

public class EditImageFilterActivity extends AppCompatActivity {

    public enum editFilter{
        None, Gray, Test
    }


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

    editFilter selectedFilter = editFilter.None;

    public native void applyRGBMinGray(long imgInputAddress, long imgOutputAddress);

    public native void applyTestFilter(long imgInputAddress, long imgOutputAddress);

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

        setContentView(R.layout.activity_edit_image_filter);

        done = findViewById(R.id.edit_done);

        editPreview = findViewById(R.id.editPreview);

        filterNone = findViewById(R.id.image_edit_filter_none);
        filterGrey = findViewById(R.id.image_edit_filter_gray);
        filterTest = findViewById(R.id.image_edit_filter_test);

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



        filterNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = editFilter.None;
                previewImage.release();
                Mat loc = new Mat(editingImageAddress).clone();
                previewImage = loc;
                updatePreviewImageView();
            }
        });

        filterGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = editFilter.Gray;
                Mat locMat = new Mat();
                previewImage.release();
                applyRGBMinGray(editingImageAddress,locMat.getNativeObjAddr());
                previewImage = locMat;
                updatePreviewImageView();
            }
        });

        filterTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:필터 이미지 블랜딩 추가
                selectedFilter = editFilter.None;
                previewImage.release();
                Mat loc = new Mat(editingImageAddress).clone();
                previewImage = loc;
                updatePreviewImageView();
            }
        });




        //편집 적용
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 프리뷰 Mat을 그대로 옮길지 아니면 프리뷰 연산 크기를 줄이고 별도로 둘지 선택할것
                switch (selectedFilter){
                    case None: //do nothing
                        break;
                    case Gray: applyRGBMinGray(editingImageAddress,editingImageAddress);
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
