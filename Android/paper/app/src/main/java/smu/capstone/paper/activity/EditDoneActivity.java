package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import smu.capstone.paper.R;

public class EditDoneActivity extends AppCompatActivity {
    Button upload, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_done);

        upload = findViewById(R.id.edit_done_upload);
        back = findViewById(R.id.edit_done_back);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDoneActivity.this, UploadDetailActivity.class);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDoneActivity.this, UploadActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}