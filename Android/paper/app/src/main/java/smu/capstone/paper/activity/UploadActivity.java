package smu.capstone.paper.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import smu.capstone.paper.R;
import smu.capstone.paper.fragment.FragUploadCam;
import smu.capstone.paper.fragment.FragUploadGal;

public class UploadActivity extends AppCompatActivity {
    private int RESULT_PERMISSIONS=100;

    final private int GALLERY = 123;
    final private int CAMERA = 456;

    private BottomNavigationView bottomNavigationView;
    FragUploadCam fragUploadCam;
    FragUploadGal fragUploadGal;
    private Toolbar toolbar;
    private FragmentManager fm;
    private FragmentTransaction ft;

    int frag_status;
    public boolean isQuick;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //편집모드 체킹
        isQuick = getIntent().getBooleanExtra("isQuick",false);


        //툴바 세팅
        toolbar = findViewById(R.id.upload_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기


        // 퍼미션 체킹
        requestPermissionCamera();

        //프레그먼트 세팅
        fragUploadCam = new FragUploadCam(isQuick);
        fragUploadGal = new FragUploadGal();

        //하단 네비게이션뷰 세팅
        bottomNavigationView = findViewById(R.id.upload_tap);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.upload_cam:
                        setFrag(CAMERA);
                        break;
                    case R.id.upload_gal:
                        setFrag(GALLERY);
                        break;
                }
                return true;
            }
        });



        setFrag(CAMERA);

    }

    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case CAMERA:
                ft.replace(R.id.upload_frame, fragUploadCam);
                ft.commit();
                frag_status = CAMERA;
                invalidateOptionsMenu();

                break;
            case GALLERY:
                ft.replace(R.id.upload_frame, fragUploadGal);
                ft.commit();
                frag_status = GALLERY ;
                invalidateOptionsMenu();
                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.next_toolbar, menu);
        if(frag_status == CAMERA){
            menu.findItem(R.id.toolbar_next).setVisible(false);
        }
        else{
            menu.findItem(R.id.toolbar_next).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }

            case R.id.toolbar_next:
                if( frag_status == GALLERY){

                    String filePath= fragUploadGal.getPicked_path();
                    if(isQuick){
                        Intent intent = new Intent(this, UploadDetailActivity.class);
                        intent.putExtra("path", filePath);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(this, DetectPaperActivity.class);
                        intent.putExtra("path", filePath);
                        startActivity(intent);
                        finish();

                    }
                }
                else if( frag_status == CAMERA){
                    //  Not happened!

                }
                return true;

        }
        return  true;
    }

    public boolean requestPermissionCamera(){
        int sdkVersion = Build.VERSION.SDK_INT;
        if(sdkVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(UploadActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        RESULT_PERMISSIONS);

            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        RESULT_PERMISSIONS);

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

