package smu.capstone.paper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.R;

public class StickerDetailActivity extends AppCompatActivity {

    int sticker_id, img, price, user_point;
    String name, comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_detail);

        ImageView sticker_img = (ImageView)findViewById(R.id.sticker_d_image);
        TextView sticker_name = (TextView)findViewById(R.id.sticker_d_name);
        TextView sticker_price = (TextView)findViewById(R.id.sticker_d_price);
        TextView sticker_com = (TextView)findViewById(R.id.sticker_d_comment);
        Button sticker_buy = (Button)findViewById(R.id.sticker_d_buy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sticker_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        // 임시로 데이터 전달 받기 --> 실제로는 stickerId만 받음
        Intent intent = getIntent();
        sticker_id = intent.getIntExtra("stickerId", 0);
        name = intent.getStringExtra("name");
        price = intent.getIntExtra("price", 0);
        img = intent.getIntExtra("image", 0);
        comment = intent.getStringExtra("comment");

        // 받아온 데이터로 셋팅
        JSONObject obj = getStickerDtData();
        try {
            sticker_name.setText(obj.getString("name"));
            sticker_price.setText(obj.getString("price") + "p");
            sticker_img.setImageResource(obj.getInt("image"));
            sticker_com.append("\n");
            sticker_com.append(obj.getString("comment"));
            user_point = obj.getInt("point");
        } catch (JSONException e){
            e.printStackTrace();
        }
        actionBar.setTitle(intent.getStringExtra("name"));

        // 구매 버튼 click listener
        sticker_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_point < price) { // 사용자 보유 포인트 부족시, 서버 통신 X
                    new AlertDialog.Builder(StickerDetailActivity.this)
                            .setMessage("포인트가 부족합니다." + "\n(보유 포인트: " + user_point + "p)")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(StickerDetailActivity.this)
                            .setMessage("스티커를 구매 할까요?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // server에 구매 요청

                                    //if resultCode == 200
                                    // sticker image저장 action
                                    //-------------------------
                                    Toast toast = Toast.makeText(StickerDetailActivity.this, "구매 완료", Toast.LENGTH_SHORT);
                                    toast.show();

                                    //if resultCode == 204
                                    //Toast toast = Toast.makeText(StickerDetailActivity.this, "오류 발생", Toast.LENGTH_SHORT);
                                    //toast.show();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });

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
            item.put("point", 1000); // 사용자의 보유 포인트도 함께 전달받음
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