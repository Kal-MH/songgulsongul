package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

public class EditImageHistogramActivity extends AppCompatActivity {

    // 기본히스토그램 평활화
    public native void equalizeHistogram(long inputImgAddress, long outputImgAddress);
    // CLAHE(구역별) 히스토그램 평활화
    public native void equalizeHistogramClahe(long inputImgAddress, long outputImgAddress);


}
