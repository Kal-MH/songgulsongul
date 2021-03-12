package smu.capstone.paper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.AddItemTagAdapter;
import smu.capstone.paper.adapter.HashTagAdapter;
import smu.capstone.paper.adapter.HomeFeedAdapter;
import smu.capstone.paper.adapter.ItemTagAdapter;
import smu.capstone.paper.adapter.PostCmtAdapter;
import smu.capstone.paper.item.ItemtagItem;

public class PostActivity extends AppCompatActivity {
    ImageButton post_setting_btn, post_like_btn, post_keep_btn;
    ListView post_cmt_list;
    PostCmtAdapter cmt_adapter;
    RecyclerView post_hashtag_rv, post_itemtag_rv;
    EditText post_input;
    Button post_write;
    ItemTagAdapter itemTagAdapter;
    HashTagAdapter hashTagAdapter;
    JSONObject post_item, post_itemtag_item, post_hashtag_item, post_cmt_item;
    TextView post_user_id, post_like_cnt, post_cmt_cnt, post_text;
    ImageView post_profile_img, post_ccl_cc, post_ccl_a, post_ccl_nc, post_ccl_nd, post_ccl_sa;
    int status;

    final int MY = 1;
    final int OTHER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        post_user_id = findViewById(R.id.post_id);
        post_like_cnt = findViewById(R.id.post_like_cnt);
        post_cmt_cnt = findViewById(R.id.post_cmt_cnt);
        post_text = findViewById(R.id.post_text);
        post_profile_img = findViewById(R.id.post_profile);
        post_ccl_cc = findViewById(R.id.post_ccl_cc);
        post_ccl_a = findViewById(R.id.post_ccl_a);
        post_ccl_nc = findViewById(R.id.post_ccl_nc);
        post_ccl_nd = findViewById(R.id.post_ccl_nd);
        post_ccl_sa = findViewById(R.id.post_ccl_sa);

        JSONObject obj1 = getPostData();
        JSONObject obj2 = getHashtagData();
        JSONObject obj3 = getItemtagData();
        JSONObject obj4 = getCmtData();

        Intent intent = getIntent();
        JSONObject obj = null;
        try {
            obj = new JSONObject(String.valueOf(intent.getIntExtra("postId",1)));
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

        try {
            itemTagAdapter = new ItemTagAdapter(post_itemtag_rv.getContext(), obj);
        } catch (JSONException e){
            e.printStackTrace();
        }
        post_itemtag_rv.setAdapter(itemTagAdapter);


        //해쉬 태그 어뎁터 설정
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        post_hashtag_rv = findViewById(R.id.post_hashtag_rv);
        post_hashtag_rv.setLayoutManager(layoutManager2);

        hashTagAdapter = new HashTagAdapter(post_hashtag_rv.getContext(), obj);
        post_hashtag_rv.setAdapter(hashTagAdapter);


        //코멘트 어뎁터 설정
        try {
            cmt_adapter = new PostCmtAdapter(post_cmt_list.getContext(), obj);
        } catch (JSONException e){
            e.printStackTrace();
        }
        post_cmt_list.setAdapter(cmt_adapter);

        post_cmt_list = (ListView) findViewById(R.id.post_cmt_list);


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
        post_like_btn.setSelected(true);
        post_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_like_btn.setSelected(!post_like_btn.isSelected());
                Log.d("TAG", "하트라고;"+ post_like_btn.isSelected());
            }
        });

        post_keep_btn=(ImageButton)findViewById(R.id.post_keep_btn);
        post_keep_btn.setSelected(true);
        post_keep_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                post_keep_btn.setSelected(!post_keep_btn.isSelected());
                Log.d("TAG", "KEEP "+ post_keep_btn.isSelected());
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

        //Status에 떄라 버튼과 포인트 visibility와 enable 설정
        switch(status){
            //본인의 계정 프로필
            case MY:
                post_setting_btn.setEnabled(true);
                post_setting_btn.setVisibility(View.VISIBLE);
                post_keep_btn.setVisibility(View.INVISIBLE);
                break;
            case OTHER:
                post_setting_btn.setEnabled(false);
                post_setting_btn.setVisibility(View.INVISIBLE);
                post_keep_btn.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
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

    //server에서 data전달
    public JSONObject getPostData(){
        post_item = new JSONObject();

        // 임시 데이터 저장
        try{
            JSONObject obj = new JSONObject();
            obj.put("userId", "wonhee");
            obj.put("timeStamp", "21-02-07");
            obj.put("likeCnt", 499);
            obj.put("comCnt", 204);
            obj.put("text", "hi everyone");
            obj.put("profileImg",R.drawable.ic_baseline_emoji_emotions_24);
            obj.put("postImg",R.drawable.sampleimg);
            obj.put("like", 0);
            obj.put("keep",0);
            obj.put("ccl1",0);
            obj.put("ccl2",0);
            obj.put("ccl3",0);
            obj.put("ccl4",0);
            obj.put("ccl5",0);

            return obj;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return post_item;
    }

    //server에서 data전달
    public JSONObject getHashtagData(){
        post_hashtag_item = new JSONObject();
        JSONArray arr= new JSONArray();

        //임시 데이터 저장
        try{
            for(int i = 0; i < 5; i++){
                JSONObject obj = new JSONObject();
                obj.put("content", "#캘리그라피");
                arr.put(obj);
            }
            post_hashtag_item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return post_hashtag_item;
    }

    //server에서 data전달
    public JSONObject getItemtagData(){
        post_itemtag_item = new JSONObject();
        JSONArray arr= new JSONArray();

        //임시 데이터 저장
        try{
            for(int i = 0; i < 5; i++){
                JSONObject obj = new JSONObject();
                obj.put("Image", R.drawable.sampleimg);
                arr.put(obj);
            }
            post_itemtag_item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return post_itemtag_item;
    }

    // server에서 data전달
    public JSONObject getCmtData(){
        post_cmt_item = new JSONObject();
        JSONArray arr= new JSONArray();

        // 임시 데이터 저장
        try{
            JSONObject obj = new JSONObject();
            obj.put("userId", "wonhee");
            obj.put("cmt", "와 정말 멋져요");
            arr.put(obj);

            JSONObject obj2 = new JSONObject();
            obj2.put("userId", "YUJIN");
            obj2.put("cmt", "좋아요 누르고 갑니다~");
            arr.put(obj2);

            JSONObject obj3 = new JSONObject();
            obj2.put("userId", "YUJIN");
            obj2.put("cmt", "안녕하세요");
            arr.put(obj2);

            post_cmt_item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return post_item;
    }

    public void setPostData(){
        JSONObject data = getPostData();
        try {
            post_user_id.setText(data.getString("userId") +"");
            post_like_cnt.setText(data.getInt("follower_count" )+"");
            post_cmt_cnt.setText(data.getInt("point") + "p");
            post_text.setText(data.getString("intro"));
            Glide.with(this).load(data.getInt("picture")).into(post_profile_img); // 게시물 사진

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
