package smu.capstone.paper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import smu.capstone.paper.R;

public class StickerDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_detail);

        ImageView sticker_img = (ImageView)findViewById(R.id.sticker_d_image);
        TextView sticker_name = (TextView)findViewById(R.id.sticker_d_name);
        TextView sticker_price = (TextView)findViewById(R.id.sticker_d_price);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sticker_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("제품명");

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        //넘어온 데이터 받아서 내용 변경
        Intent intent = getIntent();
        sticker_name.setText(intent.getStringExtra("name"));
        sticker_price.setText(intent.getStringExtra("price"));
        sticker_img.setImageResource(intent.getIntExtra("image",0));

        actionBar.setTitle(intent.getStringExtra("name"));

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