package smu.capstone.paper.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.fragment.FragFindId;

public class EditProfileActivity extends AppCompatActivity {

    private RadioGroup sns_radio;
    private RadioButton profile_sns_radio_yes, profile_sns_radio_no;
    private EditText profile_new_sns, profile_newid, profile_new_intro;
    private Button profile_img_chnage, profile_check;
    private ImageView profile_set_img;
    private JSONObject profile_item;
    private static final int REQUEST_CODE = 0;
    private int profile_sns_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("프로필 수정");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        profile_new_intro = (EditText)findViewById(R.id.profile_new_intro);
        profile_new_sns = (EditText)findViewById(R.id.profile_new_sns);
        profile_img_chnage = findViewById(R.id.profile_img_chnage);
        profile_set_img = findViewById(R.id.profile_set_img);
        sns_radio = findViewById(R.id.profile_edit_sns_radio);
        profile_newid = findViewById(R.id.profile_newid);
        profile_sns_radio_yes = findViewById(R.id.profile_sns_radio_yes);
        profile_sns_radio_no = findViewById(R.id.profile_sns_radio_no);
        profile_check = findViewById(R.id.profile_check);

        setProfileData();

        profile_img_chnage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        profile_newid.setText(LoginSharedPreference.getUserName(this));

        profile_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // newId로 server와 통신
                String newId = profile_newid.getText().toString();

                //if resultCode == 200
                new AlertDialog.Builder(EditProfileActivity.this)
                        .setMessage("사용할 수 있는 아이디입니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                //else
                /*new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("경고")
                        .setMessage("이미 사용중인 아이디입니다."+"\n"+"다시 입력해 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();*/
            }
        });

        sns_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // sns 무 선택 -> 입력창 비활성화

                if(checkedId == R.id.profile_sns_radio_no) {
                    profile_new_sns.setClickable(false);
                    profile_new_sns.setFocusable(false);
                    profile_new_sns.setHint("sns 계정 없음");
                    profile_new_sns.setText("");
                }
                // sns 유 선택 -> 입력창 활성화
                else{
                    profile_new_sns.setFocusableInTouchMode(true);
                    profile_new_sns.setFocusable(true);
                    profile_new_sns.setHint("sns 계정을 입력하세요");
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    profile_set_img.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.toolbar_save:
                return true;

            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                finish();
                break;
        }
        return  true;
    }

    //server에서 data전달
    public JSONObject getProfileData(){
        profile_item = new JSONObject();

        // 임시 데이터 저장
        try{
            JSONObject obj = new JSONObject();
            obj.put("intro", "Good to see you Buddy!");
            obj.put("sns", "https://www.google.com");
            obj.put("profile_image",R.drawable.ic_baseline_emoji_emotions_24);
            obj.put("sns_check",1);

            return obj;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return profile_item;
    }

    public void setProfileData(){

        JSONObject data = getProfileData();
        try {
            profile_new_intro.setText(data.getString("intro"));
            profile_new_sns.setText(data.getString("sns"));
            Glide.with(this).load(data.getInt("profile_image")).into(profile_set_img);
            if(data.getInt("sns_check")==0){
                profile_sns_radio_no.setChecked(true);
                profile_sns_radio_yes.setChecked(false);
                profile_new_sns.setClickable(false);
                profile_new_sns.setFocusable(false);
                profile_new_sns.setHint("sns 계정 없음");
                profile_new_sns.setText("");
            }
            else{
                profile_sns_radio_yes.setChecked(true);
                profile_sns_radio_no.setChecked(false);
                profile_new_sns.setFocusableInTouchMode(true);
                profile_new_sns.setFocusable(true);
                profile_new_sns.setHint("sns 계정을 입력하세요");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}