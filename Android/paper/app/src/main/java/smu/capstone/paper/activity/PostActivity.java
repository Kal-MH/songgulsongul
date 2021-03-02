package smu.capstone.paper.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.AddItemTagAdapter;
import smu.capstone.paper.adapter.ItemTagAdapter;
import smu.capstone.paper.adapter.PostCmtAdapter;
import smu.capstone.paper.item.ItemtagItem;

public class PostActivity extends AppCompatActivity {
    ImageButton post_back_btn;
    ImageButton post_setting_btn;
    ImageButton post_like_btn;
    ImageButton post_keep_btn;
    ListView post_cmt_list;
    PostCmtAdapter cmt_adapter;
    RecyclerView post_hashtag_rv;
    RecyclerView post_itemtag_rv;
    EditText post_input;
    Button post_write;
    ItemTagAdapter itemTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        Toolbar toolbar = (Toolbar)findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        // 아이템 태그 어뎁터 설정
        post_itemtag_rv = findViewById(R.id.post_itemtag_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        post_itemtag_rv.setLayoutManager(layoutManager);
        itemTagAdapter = new AddItemTagAdapter(this ); // 추가모드 어뎁터 세팅

        itemTagAdapter.insertItem(new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" ));
        itemTagAdapter.insertItem(new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" ));
        itemTagAdapter.insertItem(new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" ));
        itemTagAdapter.insertItem(new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" ));
        itemTagAdapter.insertItem( new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" ));

        post_itemtag_rv.setAdapter(itemTagAdapter);







        //해쉬 태그 어뎁터 설정
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        post_hashtag_rv = findViewById(R.id.post_hashtag_rv);
        post_hashtag_rv.setLayoutManager(layoutManager2);

        //코멘트 어뎁터 설정
        cmt_adapter = new PostCmtAdapter();
        post_cmt_list = (ListView) findViewById(R.id.post_cmt_list);


        //아이템 추가
        cmt_adapter.addItem("wonhee", "멋져요");
        cmt_adapter.addItem("yujin", "안녕하세요");
        cmt_adapter.addItem("yujin", "안녕하세요");
        cmt_adapter.addItem("yujin", "안녕하세요ㅎㅎ");
        cmt_adapter.addItem("yujin", "안녕하세요애호!@");
        cmt_adapter.addItem("yujin", "안녕하세요야호!!");

        post_cmt_list.setAdapter(cmt_adapter);

        post_setting_btn = (ImageButton) findViewById(R.id.post_setting_btn);
        post_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(),v);
                popup.getMenuInflater().inflate(R.menu.post_setting_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item){
                        switch(item.getItemId()){
                            case R.id.post_edit:
                                Intent intent = new Intent(PostActivity.this, PostEditActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.post_save:
                                Toast.makeText(getApplicationContext(),"이미지 저장",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.post_delete:
                                Toast.makeText(getApplicationContext(),"게시글 삭제",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        post_like_btn = (ImageButton) findViewById(R.id.post_like_btn);
        post_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_like_btn.setSelected(!post_like_btn.isSelected());
                post_like_btn.setPressed(!post_like_btn.isSelected());
            }
        });

        post_keep_btn=(ImageButton)findViewById(R.id.post_keep_btn);
        post_keep_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                post_keep_btn.setSelected(!post_keep_btn.isSelected());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
