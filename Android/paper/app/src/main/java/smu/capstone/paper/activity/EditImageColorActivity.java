package smu.capstone.paper.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import smu.capstone.paper.R;

public class EditImageColorActivity extends AppCompatActivity {

    long first_time = 0;
    long second_time = 0;


    public native void setColorHue(long inputImageAddress, long outputImageAddress , int progress);
    public native void setColorSaturation(long inputImageAddress, long outputImageAddress , int progress);
    public native void setColorBrightness(long inputImageAddress, long outputImageAddress , int progress);
    public native void setColorContrast(long inputImageAddress, long outputImageAddress , int progress);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_edit_image_colors);

    }

    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if(second_time-first_time <2000){
            super.onBackPressed();
            finish();
        }
        else{
            Toast.makeText(this,"한번 더 누르면 적용을 종료합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }

}
