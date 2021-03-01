package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.item.PostItem;

public class KeepActivity extends AppCompatActivity {

    ArrayList<PostItem> items = new ArrayList<PostItem>();
    TextView keep_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep);

        keep_count = findViewById(R.id.keep_count);
        Toolbar toolbar = (Toolbar) findViewById(R.id.keep_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("보관함");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        // view에서 id 찾아야함
        GridView gridView = findViewById(R.id.keep_grid);
        //아이템 추가
        items.add(new PostItem(R.drawable.sampleimg));
        items.add(new PostItem(R.drawable.test));
        items.add(new PostItem(R.drawable.ic_baseline_emoji_emotions_24));
        items.add(new PostItem(R.drawable.test));
        items.add(new PostItem(R.drawable.sampleimg));
        items.add(new PostItem(R.drawable.ic_favorite));
        items.add(new PostItem(R.drawable.sampleimg));

        // 어뎁터 적용
        PostImageAdapter adapter = new PostImageAdapter(this,  R.layout.post_image_item , items ) ;
        gridView.setAdapter(adapter);

        keep_count.setText("보관한 게시글 " + items.size());




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