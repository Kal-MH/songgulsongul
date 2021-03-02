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

import smu.capstone.paper.R;

public class ItemDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_detail);

        ImageView image = (ImageView)findViewById(R.id.item_detail_pic);
        TextView item_co = (TextView)findViewById(R.id.item_detail_co);
        TextView item_price = (TextView)findViewById(R.id.item_detail_price);
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
        int width = (int)(display.getWidth() * 0.9);
        int height = (int)(display.getHeight() * 0.7);
        getWindow().getAttributes().width=width;
        getWindow().getAttributes().height=height;

        // 넘겨받은 item tag정보로 내용 변경
        Intent intent = getIntent();
        item_name.setText(intent.getStringExtra("name"));
        image.setImageResource(intent.getIntExtra("image", 1));
        item_co.setText(intent.getStringExtra("co"));
        item_price.setText(intent.getStringExtra("price"));
        item_link.setText(intent.getStringExtra("link"));

    }
}