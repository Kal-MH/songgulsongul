package smu.capstone.paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    Button temp_join;
    Button temp_login;
    Button temp_home;
    Button temp_find;
    Button temp_upload;
    Button temp_realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp_join = findViewById(R.id.temp_join);
        temp_login = findViewById(R.id.temp_login);
        temp_home = findViewById(R.id.temp_home);
        temp_find = findViewById(R.id.temp_find);
        temp_upload = findViewById(R.id.temp_upload);
        temp_realm = findViewById(R.id.go_temp_realm);


        temp_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        temp_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
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

        temp_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindAccountActivity.class);
                startActivity(intent);
            }
        });

        temp_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
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



    }
}
