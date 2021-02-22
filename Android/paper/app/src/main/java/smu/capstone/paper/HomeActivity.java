package smu.capstone.paper;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragHomeFeed fragHomeFeed;
    private FragHomeComu fragHomeComu;
    private FragHomeProfile fragHomeProfile;

    public HomeActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.home_tap);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home_feed:
                        setFrag(0);
                        break;
                    case R.id.home_comu:
                        setFrag(1);
                        break;
                    case R.id.home_profile:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        fragHomeFeed = new FragHomeFeed();
        fragHomeComu = new FragHomeComu();
        fragHomeProfile = new FragHomeProfile();

        setFrag(0);

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
                ft.replace(R.id.home_frame, fragHomeComu);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.home_frame, fragHomeProfile);
                ft.commit();
                break;
        }

    }
}