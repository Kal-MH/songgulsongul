package smu.capstone.paper.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.StickerSearchAdapter;
import smu.capstone.paper.item.StickerItem;

public class StickerSearchActivity extends AppCompatActivity {

    ArrayList<StickerItem> items = new ArrayList<StickerItem>();
    public void addItem(StickerItem item){
        items.add(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sticker_search_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("검색한 제품명");

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        RecyclerView recyclerView = findViewById(R.id.sticker_search_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addItem(new StickerItem(R.drawable.test, "sample1", "10p"));
        addItem(new StickerItem(R.drawable.ic_favorite, "sample2", "20p"));
        addItem(new StickerItem(R.drawable.ic_favorite_border, "sample3", "30p"));
        addItem(new StickerItem(R.drawable.sampleimg, "sample3", "30p"));

        StickerSearchAdapter adapter = new StickerSearchAdapter(this, items);
        recyclerView.setAdapter(adapter);

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