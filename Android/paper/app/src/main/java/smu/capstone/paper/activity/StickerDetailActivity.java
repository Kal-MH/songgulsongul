package smu.capstone.paper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.R;

public class StickerDetailActivity extends AppCompatActivity {

    int sticker_id, img;
    String name, price, comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_detail);

        ImageView sticker_img = (ImageView)findViewById(R.id.sticker_d_image);
        TextView sticker_name = (TextView)findViewById(R.id.sticker_d_name);
        TextView sticker_price = (TextView)findViewById(R.id.sticker_d_price);
        TextView sticker_com = (TextView)findViewById(R.id.sticker_d_comment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sticker_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        // 임시로 데이터 전달 받기 --> 실제로는 stickerId만 받음
        Intent intent = getIntent();
        sticker_id = intent.getIntExtra("stickerId", 0);
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        img = intent.getIntExtra("image", 0);
        comment = intent.getStringExtra("comment");

        // 받아온 데이터로 셋팅
        JSONObject obj = getStickerDtData();
        try {
            sticker_name.setText(obj.getString("name"));
            sticker_price.setText(obj.getString("price"));
            sticker_img.setImageResource(obj.getInt("image"));
            sticker_com.append("\n");
            sticker_com.append(obj.getString("comment"));
        } catch (JSONException e){
            e.printStackTrace();
        }
        actionBar.setTitle(intent.getStringExtra("name"));

    }

    //server에서 data전달
    public JSONObject getStickerDtData(){
        JSONObject item = new JSONObject();

        // fraghomemarket/stickersearch activity에서 넘겨받은 stickerId로 server와 통신
        // ----------------------------------------------------------

        //임시 데이터 저장
        try{
            item.put("name", name);
            item.put("price", price);
            item.put("image", img);
            item.put("comment", comment);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}