package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import smu.capstone.paper.SettingSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.AddItemTagAdapter;

public class UploadDetailActivity extends AppCompatActivity {

    String filePath;

    RecyclerView itemtag_rv;
    AddItemTagAdapter adapter;
    EditText hashtagText;
    Switch upload_detail_ccl_1, upload_detail_ccl_2, upload_detail_ccl_3 , upload_detail_ccl_4, upload_detail_ccl_5;
    ImageView upload_detail_img;


    long first_time = 0;
    long second_time = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_detail);


        //이미지 세팅
        filePath = getIntent().getStringExtra("path");
        upload_detail_img = findViewById(R.id.upload_detail_pic);
        Glide.with(this).load(filePath).into(upload_detail_img);

        //ccl 세팅
        upload_detail_ccl_1 = findViewById(R.id.upload_detail_ccl_1);
        upload_detail_ccl_2 = findViewById(R.id.upload_detail_ccl_2);
        upload_detail_ccl_3 = findViewById(R.id.upload_detail_ccl_3);
        upload_detail_ccl_4 = findViewById(R.id.upload_detail_ccl_4);
        upload_detail_ccl_5 = findViewById(R.id.upload_detail_ccl_5);

        upload_detail_ccl_1.setChecked(SettingSharedPreference.getSetting(this,"ccl1"));
        upload_detail_ccl_2.setChecked(SettingSharedPreference.getSetting(this,"ccl2"));
        upload_detail_ccl_3.setChecked(SettingSharedPreference.getSetting(this,"ccl3"));
        upload_detail_ccl_4.setChecked(SettingSharedPreference.getSetting(this,"ccl4"));
        upload_detail_ccl_5.setChecked(SettingSharedPreference.getSetting(this,"ccl5"));

        // 툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.upload_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        //해쉬태그 작성
        hashtagText = findViewById(R.id.upload_hashtag);
        hashtagText.append("#");
        hashtagText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if(source.charAt(i) == ' ')
                        return " #";
                    else if(source.charAt(i) == '#')
                        continue;
                    else if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        }});

        hashtagText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG", charSequence.length() + ":" + charSequence+"");
                if(charSequence.length()==0)
                    hashtagText.append("#");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


/*

        // 아이템 태그 어뎁터 설정
        itemtag_rv = findViewById(R.id.upload_itemtag_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        itemtag_rv.setLayoutManager(layoutManager);
        JSONObject itemtag_obj = getItemtagData();

        try {
            adapter = new AddItemTagAdapter(itemtag_rv.getContext(),itemtag_obj ); // 추가모드 어뎁터 세팅
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 적용
        itemtag_rv.setAdapter(adapter);

*/

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JSONObject getItemtagData(){
        JSONObject post_itemtag_item = new JSONObject();
        JSONArray arr= new JSONArray();


        //임시 데이터 저장
        try{

            JSONObject obj2 = new JSONObject();
            obj2.put("Image", drawable2Bitmap( getDrawable(R.drawable.ic_baseline_add_24)) );
            arr.put(obj2);


            for(int i = 0; i < 5; i++){
                JSONObject obj = new JSONObject();
                obj.put("Image", drawable2Bitmap( getDrawable(R.drawable.sampleimg)) );
                arr.put(obj);
            }
            post_itemtag_item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return post_itemtag_item;
    }


    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if(second_time-first_time <2000){
            super.onBackPressed();
            finish();
        }
        else{
            Toast.makeText(this,"한번 더 누르면 업로드를 종료합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
               // 알림 팝업
                return true;

            case R.id.toolbar_done :
                // 서버에 변경된 data(객체) 전송

                //if resultCode == 200
                Toast toast = Toast.makeText(UploadDetailActivity.this, "업로드 완료", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(UploadDetailActivity.this, PostActivity.class); // 업데이트 된 게시물로 다시 이동
                startActivity(intent);

                //if resultCode == 500
                /*Toast toast = Toast.makeText(UploadDetailActivity.this, "업로드 실패", Toast.LENGTH_SHORT);
                toast.show();*/
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}