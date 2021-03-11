package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.item.PostItem;

public class KeepActivity extends AppCompatActivity {

    PostImageAdapter adapter;
    TextView keep_count, keep_id;
    ImageView keep_imae;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep);

        keep_count = findViewById(R.id.keep_count);
        keep_id = findViewById(R.id.keep_id);
        keep_imae = findViewById(R.id.keep_imae);
        Toolbar toolbar = (Toolbar) findViewById(R.id.keep_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("보관함");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        // view에서 id 찾아야함
        GridView gridView = findViewById(R.id.keep_grid);

        final JSONObject obj = getKeepData();

        // 어뎁터 적용
        try {
            adapter = new PostImageAdapter(this, R.layout.post_image_item, obj);
        } catch (JSONException e){
            e.printStackTrace();
        }
        gridView.setAdapter(adapter);

        // ProfileActivity에서 전달받은 프로필 사진으로 셋팅
        //Intent intent = getIntent();
        //keep_imae.setImageResource(intent.getIntExtra("profileImg", 0));

        // 로그인한 userId로 셋팅
        //keep_id.setText(userId);

        // 보관한 게시글 개수 셋팅
        try {
            keep_count.setText("보관한 게시글 " + obj.getJSONArray("data").length());
        } catch (JSONException e){
            e.printStackTrace();
        }

        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(KeepActivity.this, PostActivity.class);

                // 게시글 id 전달
                try {
                    int postId = obj.getJSONArray("data").getJSONObject(position).getInt("postId");
                    intent.putExtra("postId", postId);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                startActivity(intent);

                Log.d("TAG", position + "is Clicked");      // Can not getting this method.
            }
        });

    }

    //server에서 data전달
    public JSONObject getKeepData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();

        //임시 데이터 저장
        try{
            JSONObject obj1 = new JSONObject();
            obj1.put("postImage", R.drawable.sampleimg);
            obj1.put("postId", 1);
            arr.put(obj1);

            JSONObject obj2 = new JSONObject();
            obj2.put("postImage", R.drawable.test);
            obj2.put("postId", 2);
            arr.put(obj2);

            JSONObject obj3 = new JSONObject();
            obj3.put("postImage", R.drawable.ic_baseline_emoji_emotions_24);
            obj3.put("postId", 3);
            arr.put(obj3);

            JSONObject obj4 = new JSONObject();
            obj4.put("postImage", R.drawable.test);
            obj4.put("postId", 4);
            arr.put(obj4);

            JSONObject obj5 = new JSONObject();
            obj5.put("postImage", R.drawable.sampleimg);
            obj5.put("postId", 5);
            arr.put(obj5);

            JSONObject obj6 = new JSONObject();
            obj6.put("postImage", R.drawable.ic_favorite);
            obj6.put("postId", 6);
            arr.put(obj6);

            JSONObject obj7 = new JSONObject();
            obj7.put("postImage", R.drawable.sampleimg);
            obj7.put("postId", 7);
            arr.put(obj7);

            item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                Log.d("TAG", "뒤로,,,");
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}