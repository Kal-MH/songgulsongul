package com.smu.songgulsongul.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.smu.songgulsongul.SettingSharedPreference;
import com.smu.songgulsongul.R;

public class SettingActivity extends AppCompatActivity {
    SwitchCompat setting_ccl_1, setting_ccl_2, setting_ccl_3, setting_ccl_4, setting_ccl_5
            , setting_alert_1, setting_alert_2, setting_alert_3, setting_alert_4;
    boolean ccl1, ccl2, ccl3, ccl4, ccl5
            , alert1, alert2, alert3, alert4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting_ccl_1 = findViewById(R.id.setting_ccl_1);
        setting_ccl_2 = findViewById(R.id.setting_ccl_2);
        setting_ccl_3 = findViewById(R.id.setting_ccl_3);
        setting_ccl_4 = findViewById(R.id.setting_ccl_4);
        setting_ccl_5 = findViewById(R.id.setting_ccl_5);
        setting_alert_1 = findViewById(R.id.setting_alert_1);
        setting_alert_2 = findViewById(R.id.setting_alert_2);
        setting_alert_3 = findViewById(R.id.setting_alert_3);
        setting_alert_4 = findViewById(R.id.setting_alert_4);


        //툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("개인 설정");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        ccl1 = SettingSharedPreference.getSetting(this,"ccl1");
        ccl2 = SettingSharedPreference.getSetting(this,"ccl2");
        ccl3 = SettingSharedPreference.getSetting(this,"ccl3");
        ccl4 = SettingSharedPreference.getSetting(this,"ccl4");
        ccl5 = SettingSharedPreference.getSetting(this,"ccl5");
        alert1 = SettingSharedPreference.getSetting(this,"alert1");
        alert2 = SettingSharedPreference.getSetting(this,"alert2");
        alert3 = SettingSharedPreference.getSetting(this,"alert3");
        alert4 = SettingSharedPreference.getSetting(this,"alert4");

        setting_ccl_1.setChecked(ccl1);
        setting_ccl_2.setChecked(ccl2);
        setting_ccl_3.setChecked(ccl3);
        setting_ccl_4.setChecked(ccl4);
        setting_ccl_5.setChecked(ccl5);
        setting_alert_1.setChecked(alert1);
        setting_alert_2.setChecked(alert2);
        setting_alert_3.setChecked(alert3);
        setting_alert_4.setChecked(alert4);


        setting_ccl_1.setOnCheckedChangeListener(checkedChangeListener);
        setting_ccl_2.setOnCheckedChangeListener(checkedChangeListener);
        setting_ccl_3.setOnCheckedChangeListener(checkedChangeListener);
        setting_ccl_4.setOnCheckedChangeListener(checkedChangeListener);
        setting_ccl_5.setOnCheckedChangeListener(checkedChangeListener);
        setting_alert_1.setOnCheckedChangeListener(checkedChangeListener);
        setting_alert_2.setOnCheckedChangeListener(checkedChangeListener);
        setting_alert_3.setOnCheckedChangeListener(checkedChangeListener);
        setting_alert_4.setOnCheckedChangeListener(checkedChangeListener);

        // 워터마크 스피너


    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.setting_ccl_1:
                    if(isChecked)
                        ccl1=setting_ccl_1.isChecked();
                    break;
                case R.id.setting_ccl_2:
                    if(isChecked)
                        ccl2=setting_ccl_2.isChecked();
                    break;
                case R.id.setting_ccl_3:
                    if(isChecked)
                        ccl3=setting_ccl_3.isChecked();
                    break;
                case R.id.setting_ccl_4:
                    if(isChecked)
                        ccl4=setting_ccl_4.isChecked();
                    break;
                case R.id.setting_ccl_5:
                    if(isChecked)
                        ccl5=setting_ccl_5.isChecked();
                    break;
                case R.id.setting_alert_1:
                    if(isChecked)
                        alert1=setting_alert_1.isChecked();
                    break;
                case R.id.setting_alert_2:
                    if(isChecked)
                        alert2=setting_alert_2.isChecked();
                    break;
                case R.id.setting_alert_3:
                    if(isChecked)
                        alert3=setting_alert_3.isChecked();
                    break;
                case R.id.setting_alert_4:
                    if(isChecked)
                        alert4=setting_alert_4.isChecked();
                    break;
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_save:
                SettingSharedPreference.setSetting(SettingActivity.this, "ccl1",ccl1);
                SettingSharedPreference.setSetting(SettingActivity.this, "ccl2",ccl2);
                SettingSharedPreference.setSetting(SettingActivity.this, "ccl3",ccl3);
                SettingSharedPreference.setSetting(SettingActivity.this, "ccl4",ccl4);
                SettingSharedPreference.setSetting(SettingActivity.this, "ccl5",ccl5);
                SettingSharedPreference.setSetting(SettingActivity.this, "alert1",alert1);
                SettingSharedPreference.setSetting(SettingActivity.this, "alert2",alert2);
                SettingSharedPreference.setSetting(SettingActivity.this, "alert3",alert3);
                SettingSharedPreference.setSetting(SettingActivity.this, "alert4",alert4);
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_toolbar, menu);

        return true;
    }

}