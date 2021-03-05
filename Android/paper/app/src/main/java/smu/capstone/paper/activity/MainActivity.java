package smu.capstone.paper.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import smu.capstone.paper.R;
import smu.capstone.paper.RealmTest;

public class MainActivity extends AppCompatActivity {

    Button temp_login;
    Button temp_home;
    Button temp_realm;
    Button temp_postedit;
    Button temp_post;

    Button temp_item;
    Button temp_save;


    private int RESULT_PERMISSIONS=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // 퍼미션 체킹
        requestPermissionCamera();



        temp_login = findViewById(R.id.temp_login);
        temp_home = findViewById(R.id.temp_home);
        temp_realm = findViewById(R.id.go_temp_realm);
        temp_postedit = findViewById(R.id.temp_edit);
        temp_post = findViewById(R.id.temp_post);

        temp_item = findViewById(R.id.temp_item);
        temp_save = findViewById(R.id.temp_save);


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


        temp_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class);
                // 선택한 item 정보 전달
                intent.putExtra("name", "item1");
                intent.putExtra("image", R.drawable.ic_baseline_create_24);
                intent.putExtra("co", "모나미");
                intent.putExtra("price","500원");
                intent.putExtra("link", "http://shopping.naver.com");
                startActivity(intent);
            }
        });


        temp_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SaveImageActivity.class);
                intent.putExtra("image", R.drawable.sampleimg);
                startActivity(intent);

            }
        });

    }


    public boolean requestPermissionCamera(){
        int sdkVersion = Build.VERSION.SDK_INT;
        if(sdkVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED   ) {

                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        RESULT_PERMISSIONS
                );

            }

        }else{  // version 6 이하일때
            // setInit();
            return true;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (RESULT_PERMISSIONS == requestCode) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허가시
                //setInit();
            } else {
                // 권한 거부시
            }
            return;
        }

    }

}