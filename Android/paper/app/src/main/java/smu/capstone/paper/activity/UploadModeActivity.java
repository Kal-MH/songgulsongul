package smu.capstone.paper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import smu.capstone.paper.R;

public class UploadModeActivity extends Activity {

    ImageButton quickbtn;
    ImageButton normalbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_mode);

        quickbtn = findViewById(R.id.upload_mode_quick_btn);
        normalbtn= findViewById(R.id.upload_mode_normal_btn);


      /*  Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int)(display.getWidth()* 0.9);
        int height = (int)(display.getHeight() * 0.9);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
*/

        quickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadModeActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        normalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadModeActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });


    }
}