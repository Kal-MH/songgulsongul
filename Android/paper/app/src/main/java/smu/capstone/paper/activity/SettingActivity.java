package smu.capstone.paper.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.Array;

import smu.capstone.paper.CclSharedPreference;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;

public class SettingActivity extends AppCompatActivity {
    Switch setting_ccl_1, setting_ccl_2, setting_ccl_3, setting_ccl_4, setting_ccl_5;
    boolean ccl1, ccl2, ccl3, ccl4, ccl5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting_ccl_1 = findViewById(R.id.setting_ccl_1);
        setting_ccl_2 = findViewById(R.id.setting_ccl_2);
        setting_ccl_3 = findViewById(R.id.setting_ccl_3);
        setting_ccl_4 = findViewById(R.id.setting_ccl_4);
        setting_ccl_5 = findViewById(R.id.setting_ccl_5);

        //툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("개인 설정");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        setting_ccl_1.setChecked(CclSharedPreference.getPrefCcl(this,"ccl1"));
        setting_ccl_2.setChecked(CclSharedPreference.getPrefCcl(this,"ccl2"));
        setting_ccl_3.setChecked(CclSharedPreference.getPrefCcl(this,"ccl3"));
        setting_ccl_4.setChecked(CclSharedPreference.getPrefCcl(this,"ccl4"));
        setting_ccl_5.setChecked(CclSharedPreference.getPrefCcl(this,"ccl5"));

        setting_ccl_1.setOnCheckedChangeListener(checkedChangeListener);
        setting_ccl_2.setOnCheckedChangeListener(checkedChangeListener);
        setting_ccl_3.setOnCheckedChangeListener(checkedChangeListener);
        setting_ccl_4.setOnCheckedChangeListener(checkedChangeListener);
        setting_ccl_5.setOnCheckedChangeListener(checkedChangeListener);

        // 워터마크 스피너


    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.setting_ccl_1:
                    if(isChecked)
                        ccl1=true;
                    else
                        ccl1=false;
                    break;
                case R.id.setting_ccl_2:
                    if(isChecked)
                        ccl2=true;

                    else
                        ccl2=false;
                    break;
                case R.id.setting_ccl_3:
                    if(isChecked)
                        ccl3=true;
                    else
                        ccl3=false;
                    break;
                case R.id.setting_ccl_4:
                    if(isChecked)
                        ccl4=true;
                    else
                        ccl4=false;
                    break;
                case R.id.setting_ccl_5:
                    if(isChecked)
                        ccl5=true;
                    else
                        ccl5=false;
                    break;
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_save:
                CclSharedPreference.setPrefCcl(SettingActivity.this, "ccl1",ccl1);
                CclSharedPreference.setPrefCcl(SettingActivity.this, "ccl2",ccl2);
                CclSharedPreference.setPrefCcl(SettingActivity.this, "ccl3",ccl3);
                CclSharedPreference.setPrefCcl(SettingActivity.this, "ccl4",ccl4);
                CclSharedPreference.setPrefCcl(SettingActivity.this, "ccl5",ccl5);
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