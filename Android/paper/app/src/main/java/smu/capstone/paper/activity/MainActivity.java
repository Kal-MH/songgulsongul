package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;
import smu.capstone.paper.R;
import smu.capstone.paper.RealmTest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button temp_login;
    Button temp_home;
    Button temp_realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp_login = findViewById(R.id.temp_login);
        temp_home = findViewById(R.id.temp_home);
        temp_realm = findViewById(R.id.go_temp_realm);


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



    }
}