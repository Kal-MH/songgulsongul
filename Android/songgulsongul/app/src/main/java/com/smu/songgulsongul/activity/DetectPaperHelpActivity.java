package com.smu.songgulsongul.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.smu.songgulsongul.R;

public class DetectPaperHelpActivity  extends Activity {

    Button again, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detect_paper_help);

        again = findViewById(R.id.dp_help_again);
        done = findViewById(R.id.dp_help_ok);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다시 UploadActivity로 전환


            }
        });


    }
}