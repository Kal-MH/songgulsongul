package smu.capstone.paper.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.StickerSearchAdapter;
import smu.capstone.paper.item.StickerItem;

public class StickerSearchActivity extends AppCompatActivity {

    StickerSearchAdapter adapter;
    TextView sticker_sort_price;
    TextView sticker_sort_data;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_search);

        Intent intent = getIntent();
        sticker_sort_price = (TextView)findViewById(R.id.sticker_sort_price);
        sticker_sort_data = (TextView)findViewById(R.id.sticker_sort_date);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sticker_search_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(intent.getStringExtra("search"));

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        // 낮은 가격순 정렬
        sticker_sort_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서버에 정렬 요청
                // --------------------------

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        // 최신순 정렬
       sticker_sort_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서버에 정렬 요청
                // ---------------------

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.sticker_search_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        JSONObject obj = getStickerData();

        try {
            adapter = new StickerSearchAdapter(this, obj);
        } catch (JSONException e){
            e.printStackTrace();
        }
        recyclerView.setAdapter(adapter);

    }

    public JSONObject getStickerData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();

        // fraghomemarket activity에서 넘겨받은 query로 서버와 통신
        // ------------------------------------------------------

        try{
            JSONObject obj1 = new JSONObject();
            obj1.put("name", "sample1");
            obj1.put("price", "10");
            obj1.put("image", R.drawable.test);
            obj1.put("id", 1);
            obj1.put("text", "스티커 샘플 1 입니다~");
            arr.put(obj1);

            JSONObject obj2 = new JSONObject();
            obj2.put("name", "sample2");
            obj2.put("price", "20");
            obj2.put("image", R.drawable.ic_favorite);
            obj2.put("id", 2);
            obj2.put("text", "스티커 샘플 2 입니다~");
            arr.put(obj2);

            JSONObject obj3 = new JSONObject();
            obj3.put("name", "sample3");
            obj3.put("price", "30");
            obj3.put("image", R.drawable.ic_favorite_border);
            obj3.put("id", 3);
            obj3.put("text", "스티커 샘플 3 입니다~");
            arr.put(obj3);

            JSONObject obj4 = new JSONObject();
            obj4.put("name", "sample4");
            obj4.put("price", "40");
            obj4.put("image", R.drawable.sampleimg);
            obj4.put("id", 4);
            obj4.put("text", "스티커 샘플 4 입니다~");
            arr.put(obj4);

            item.put("data", arr);
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