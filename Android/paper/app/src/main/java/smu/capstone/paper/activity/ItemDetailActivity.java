package smu.capstone.paper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import smu.capstone.paper.R;
import smu.capstone.paper.server.RetrofitClient;

public class ItemDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_detail);

        ImageView image = (ImageView)findViewById(R.id.item_detail_pic);
        TextView item_hprice = (TextView)findViewById(R.id.item_detail_hprice);
        TextView item_lprice = (TextView)findViewById(R.id.item_detail_lprice);
        TextView close = (TextView)findViewById(R.id.item_detail_close);
        TextView item_name = (TextView)findViewById(R.id.item_detail_name);
        TextView item_link = (TextView)findViewById(R.id.item_detail_link);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int)(display.getWidth() * 0.98);
        getWindow().getAttributes().width=width;

        // 넘겨받은 item tag정보로 내용 변경
        Intent intent = getIntent();
        item_name.setText(intent.getStringExtra("name"));
        Glide.with(ItemDetailActivity.this).load(RetrofitClient.getBaseUrl() + intent.getStringExtra("picture")).into(image); // 게시물 사진
        item_lprice.setText(intent.getIntExtra("lprice" , 0) + " ");
        item_hprice.setText(intent.getIntExtra("hprice" , 0) + " ");
        item_link.setText(intent.getStringExtra("url"));

    }
}