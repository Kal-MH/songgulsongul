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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import smu.capstone.paper.R;

public class EditImageRatioActivity extends AppCompatActivity {

    long first_time = 0;
    long second_time = 0;

    String filePath;

    long paperImgAddress;
    long croppedImgAddress;
    long editingImageAddress;
    long editingImageRatioAddress;



    Mat preEditImage;
    Mat previewImage;

    SeekBar ratioSeekBar;
    Button done;

    ImageView editPreview;
    Bitmap editPreviewBitmap;

    public native void changeImageRatio(long inputImgAddress, long outputImgAddress ,int seekBarProgress);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image_ratio);

        ratioSeekBar = findViewById(R.id.ratioSeekBar);
        done = findViewById(R.id.edit_done);
        editPreview = findViewById(R.id.editPreview);


        filePath = getIntent().getStringExtra("path");
        //preEditImage = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_COLOR);
        //Imgproc.cvtColor(preEditImage,preEditImage, Imgproc.COLOR_BGR2RGB);//RGB BGR 채널 뒤밖임 수정


        editingImageAddress = getIntent().getLongExtra("editingImageAddress", 0);
        try{
            preEditImage = new Mat(editingImageAddress).clone();//주소에서 파일복사 //TODO: 메모리 누수 체크
            //편집 취소해도 연동되지 않게 별도 객체로 분리
            //previewImage.copyTo(previewImage);
            previewImage = preEditImage.clone();
            editPreviewBitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
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

                changeImageRatio(preEditImage.getNativeObjAddr(), editingImageAddress, ratioSeekBar.getProgress());
                finish();
            }
        });

        ratioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    if(previewImage==null)
                        previewImage = preEditImage.clone();


                    previewImage.release();
                    Mat locMat = new Mat();
                    changeImageRatio(preEditImage.getNativeObjAddr(),locMat.getNativeObjAddr(),progress);
                    previewImage = locMat;
                    editPreviewBitmap.recycle();

                    editPreviewBitmap = Bitmap.createBitmap(previewImage.cols(),previewImage.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(previewImage, editPreviewBitmap);
                    editPreview.setImageBitmap(editPreviewBitmap);
                    //System.gc();
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

    @Override
    public void finish() {
        previewImage.release();
        preEditImage.release();
        super.finish();
    }
}
