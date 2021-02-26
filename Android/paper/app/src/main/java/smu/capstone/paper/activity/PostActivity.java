package smu.capstone.paper.activity;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostCmtAdapter;
import smu.capstone.paper.item.HomeMarketItem;
import smu.capstone.paper.item.PostCmtItem;
import smu.capstone.paper.item.PostItem;

public class PostActivity extends AppCompatActivity {
    ImageButton post_back_btn;
    ImageButton post_setting_btn;
    ImageButton post_like;
    ListView post_cmt_list;
    PostCmtAdapter cmt_adapter;
    RecyclerView post_hashtag_rv;
    RecyclerView post_itemtag_rv;
    EditText post_input;
    Button post_write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        cmt_adapter = new PostCmtAdapter();
        post_cmt_list = (ListView) findViewById(R.id.post_cmt_list);
        post_cmt_list.setAdapter(cmt_adapter);

        cmt_adapter.addItem("wonhee", "멋져요");
        cmt_adapter.addItem("yujin", "안녕하세요");
        cmt_adapter.notifyDataSetChanged(); //어댑터의 변경을 알림.

        post_back_btn = (ImageButton)findViewById(R.id.join_back_btn);
        post_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        post_setting_btn = (ImageButton) findViewById(R.id.post_setting_btn);
        post_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostActivity.this, "설정 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        post_like = (ImageButton) findViewById(R.id.post_like);
        post_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_like.setSelected(!post_like.isSelected());
            }
        });

        post_input = (EditText) findViewById(R.id.post_input);
        post_write = (Button) findViewById(R.id.post_write);

        // 텍스트 입력시 로그인 버튼 활성화
        post_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    post_write.setClickable(true);
                    post_write.setBackgroundColor(0x9A93C8B4);
                }
            }
        });
    }

}
