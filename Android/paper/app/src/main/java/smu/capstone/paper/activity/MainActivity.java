package smu.capstone.paper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import smu.capstone.paper.R;
import smu.capstone.paper.RealmTest;

public class MainActivity extends AppCompatActivity {

    Button temp_login;
    Button temp_home;
    Button temp_realm;
    Button temp_postedit;
    Button temp_post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp_login = findViewById(R.id.temp_login);
        temp_home = findViewById(R.id.temp_home);
        temp_realm = findViewById(R.id.go_temp_realm);
        temp_postedit = findViewById(R.id.temp_edit);
        temp_post = findViewById(R.id.temp_post);

        temp_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });


        temp_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        temp_realm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RealmTest.class);
                startActivity(intent);

            }
        });

        temp_postedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PostEditActivity.class);
                startActivity(intent);

            }
        });

        temp_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                startActivity(intent);

            }
        });

    }
}