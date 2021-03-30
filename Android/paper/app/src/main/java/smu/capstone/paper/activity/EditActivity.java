package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import smu.capstone.paper.R;

public class EditActivity extends AppCompatActivity {

    String filePath;
    ImageView edit_iv;
    Button back, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        done = findViewById(R.id.edit_done);
        back = findViewById(R.id.edit_back);

        filePath = getIntent().getStringExtra("path");
        edit_iv = findViewById(R.id.edit_pic);
        Glide.with(this).load(filePath).into(edit_iv);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( EditActivity.this , EditDoneActivity.class);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
    }
}