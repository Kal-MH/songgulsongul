package com.smu.songgulsongul.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.fragment.FragHomeComu;
import com.smu.songgulsongul.fragment.FragHomeFeed;
import com.smu.songgulsongul.fragment.FragHomeMarket;
import com.smu.songgulsongul.responseData.CodeResponse;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragHomeFeed fragHomeFeed;
    private FragHomeComu fragHomeComu;
    private FragHomeMarket fragHomeMarket;
    private Toolbar toolbar;


    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setTitle("송글손글");


        bottomNavigationView = findViewById(R.id.home_tab);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home_feed:
                        setFrag(0);
                        break;
                    case R.id.home_market:
                        setFrag(1);
                        break;

                    case R.id.home_board:

                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        fragHomeFeed = new FragHomeFeed();
        fragHomeComu = new FragHomeComu();
        fragHomeMarket = new FragHomeMarket();

        setFrag(0);

        Button tmp = findViewById(R.id.home_temp);
        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceApi.PushAlarm(LoginSharedPreference.getToken(HomeActivity.this))
                        .enqueue(new Callback<CodeResponse>() {
                            @Override
                            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {

                            }
                            @Override
                            public void onFailure(Call<CodeResponse> call, Throwable t) {
                                Toast.makeText(HomeActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                                // 이부분 미완성 성공일때도 onFailure 실행됨됨
                           }
                        });
            }
        });

    }

    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.home_frame, fragHomeFeed);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.home_frame, fragHomeMarket);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.home_frame, fragHomeComu);
                ft.commit();
                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home_profile:
                Intent intent= new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("userId", LoginSharedPreference.getLoginId(HomeActivity.this));
                startActivity(intent);
                return true;

            case R.id.home_add:
                Intent intent2= new Intent(HomeActivity.this, UploadModeActivity.class);
                startActivity(intent2);
                return true;

        }
        return  true;
    }
}