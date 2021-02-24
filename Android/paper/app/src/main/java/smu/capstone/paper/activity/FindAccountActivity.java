package smu.capstone.paper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import smu.capstone.paper.R;
import smu.capstone.paper.fragment.FragFindId;

public class FindAccountActivity extends AppCompatActivity {

    Button find_id_btn, find_pw_btn;
    ImageButton find_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        find_id_btn = (Button)findViewById(R.id.find_id_btn);
        find_pw_btn = (Button)findViewById(R.id.find_pw_btn);
        find_back_btn = (ImageButton)findViewById(R.id.find_back_btn);


        find_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        find_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragFindId fragFindId = new FragFindId();
                transaction.replace(R.id.find_frame, fragFindId);
                transaction.commit();
            }
        });

        find_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragFindId.FragHomeProfile.FragFindPw fragFindPw = new FragFindId.FragHomeProfile.FragFindPw();
                transaction.replace(R.id.find_frame, fragFindPw);
                transaction.commit();
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragFindId fragFindId = new FragFindId();
        transaction.replace(R.id.find_frame, fragFindId);
        transaction.commit();
    }
}