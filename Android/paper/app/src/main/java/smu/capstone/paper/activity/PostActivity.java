package smu.capstone.paper.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.HashTagAdapter;
import smu.capstone.paper.adapter.ItemTagAdapter;
import smu.capstone.paper.adapter.PostCmtAdapter;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class PostActivity extends AppCompatActivity {
    ImageButton post_setting_btn, post_like_btn, post_keep_btn;
    ListView post_cmt_list;
    RecyclerView post_hashtag_rv, post_itemtag_rv;

    EditText post_input;
    Button post_write;

    PostCmtAdapter cmt_adapter;
    ItemTagAdapter itemTagAdapter;
    HashTagAdapter hashTagAdapter;

    TextView post_user_id, post_like_cnt, post_cmt_cnt, post_text, post_date;
    ImageView post_pic, post_profile, post_ccl_cc, post_ccl_a, post_ccl_nc, post_ccl_nd, post_ccl_sa;
    boolean ccl1, ccl2, ccl3, ccl4, ccl5;
    int status;

    final int MY = 1;
    final int OTHER = 2;


    int user_id, post_id;
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;
    JsonObject data, postData, userData;
    JsonArray hashTagsData,  itemTagData, CommentsData;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        {
            post_write = (Button) findViewById(R.id.post_write);
            post_keep_btn=(ImageButton)findViewById(R.id.post_keep_btn);
            post_like_btn = (ImageButton) findViewById(R.id.post_like_btn);
            post_setting_btn = (ImageButton) findViewById(R.id.post_setting_btn);
            post_input = (EditText) findViewById(R.id.post_input);
            post_user_id = findViewById(R.id.post_id);
            post_like_cnt = findViewById(R.id.post_like_cnt);
            post_cmt_cnt = findViewById(R.id.post_cmt_cnt);
            post_text = findViewById(R.id.post_text);
            post_profile = findViewById(R.id.post_profile);
            post_pic = findViewById(R.id.post_pic);
            post_date = findViewById(R.id.post_date);
            post_ccl_cc = findViewById(R.id.post_ccl_cc);
            post_ccl_a = findViewById(R.id.post_ccl_a);
            post_ccl_nc = findViewById(R.id.post_ccl_nc);
            post_ccl_nd = findViewById(R.id.post_ccl_nd);
            post_ccl_sa = findViewById(R.id.post_ccl_sa);
            post_itemtag_rv = findViewById(R.id.post_itemtag_rv);
            post_cmt_list = (ListView) findViewById(R.id.post_cmt_list);
            post_hashtag_rv = findViewById(R.id.post_hashtag_rv);
            post_cmt_list = findViewById(R.id.post_cmt_list);
        }

        //툴바 세팅
        Toolbar toolbar = (Toolbar)findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        Intent intent = getIntent();
        user_id = LoginSharedPreference.getUserId(PostActivity.this);
        post_id = intent.getIntExtra("post_id",-1);

        Log.d("TAG", user_id +":" + post_id);
        //서버통신
        getData();


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
                                // postEditActivity로 기존 data 전달

                                intent.putExtra("postImg", postData.get("image").getAsString());
                                intent.putExtra("text", postData.get("text").getAsString());
                              //  intent.putExtra("hashtag",  ?? );
                             //   intent.putExtra("itemtag", ??);

                                /*
                                intent.putExtra("ccl1",post_obj.getBoolean("ccl1"));
                                intent.putExtra("ccl2",post_obj.getBoolean("ccl2"));
                                intent.putExtra("ccl3",post_obj.getBoolean("ccl3"));
                                intent.putExtra("ccl4",post_obj.getBoolean("ccl4"));
                                intent.putExtra("ccl5",post_obj.getBoolean("ccl5"));*/


                                startActivity(intent);
                                break;
                            case R.id.post_save:
                               /* Intent intent2 = new Intent(PostActivity.this, SaveImageActivity.class);
                                intent2.putExtra("postImg", post_obj.getInt("image"));

                                startActivity(intent2);*/
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

        post_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_like_btn.setSelected(!post_like_btn.isSelected());
                Log.d("TAG", "하트라고;"+ post_like_btn.isSelected());
            }
        });
        post_keep_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                post_keep_btn.setSelected(!post_keep_btn.isSelected());
                Log.d("TAG", "KEEP "+ post_keep_btn.isSelected());
            }
        });

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

    public void getData(){
        serviceApi.GetDetailPost(post_id, user_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();

                int resultCode = result.get("code").getAsInt();

                if( resultCode == statusCode.RESULT_OK){
                    data = result.getAsJsonObject("data");
                    setPostData();
                    setHashTagData();
                    setItemTagData();
                    setCommentsData();

                }
                else if(resultCode == statusCode.RESULT_SERVER_ERR){
                    Toast.makeText(PostActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    // 빈화면 말고 다른행동..

                }
                else {
                    Toast.makeText(PostActivity.this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PostActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });


    }

    public boolean setPostData(){
        postData = data.getAsJsonObject("post");
        userData = data.getAsJsonObject("user");

        //작성자 프로필
        post_user_id.setText(userData.get("login_id").getAsString());
        Glide.with(this).load(RetrofitClient.getBaseUrl() + userData.get("img_profile").getAsString()).into(post_profile);

        // 게시글 정보 세팅
        post_date.setText(postData.get("post_date").getAsString() + postData.get("post_time").getAsString());
        post_text.setText(postData.get("text").getAsString());
        Glide.with(this).load(RetrofitClient.getBaseUrl() + postData.get("image").getAsString()).into(post_pic);


        // data 세팅
        post_like_cnt.setText("좋아요 " + data.get("likeNum").getAsInt());
        if( data.get("likeOnset").getAsInt() == 1)
            post_like_btn.setSelected(true);
        else
            post_like_btn.setSelected(false);

        if( data.get("keepOnset").getAsInt() == 1)
            post_keep_btn.setSelected(true);
        else
            post_keep_btn.setSelected(false);



        //CCL 세팅 ! 서버 코딩내용없어서 .. 놔둠..
       /* ccl1=postData.getBoolean("ccl1");
        ccl2=postData.getBoolean("ccl2");
        ccl3=postData.getBoolean("ccl3");
        ccl4=postData.getBoolean("ccl4");
        ccl5=postData.getBoolean("ccl5");


        if(ccl1)
            post_ccl_cc.setImageResource(R.drawable.ccl_cc_fill);
        if(ccl2)
            post_ccl_a.setImageResource(R.drawable.ccl_attribution_fill);
        if(ccl3)
            post_ccl_nc.setImageResource(R.drawable.ccl_noncommercial_fill);
        if(ccl4)
            post_ccl_nd.setImageResource(R.drawable.ccl_no_derivative_fill);
        if(ccl5)
            post_ccl_sa.setImageResource(R.drawable.ccl_share_alike_fill);
*/

        return true;
    }
    public boolean setHashTagData(){

        hashTagsData = data.getAsJsonArray("hashTags");
        //해쉬 태그 어뎁터 설정
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        post_hashtag_rv.setLayoutManager(layoutManager2);

        hashTagAdapter = new HashTagAdapter(post_hashtag_rv.getContext(), hashTagsData);
        post_hashtag_rv.setAdapter(hashTagAdapter);
        return true;
    }
    public boolean setItemTagData(){
        itemTagData = data.getAsJsonArray("itemTags");
        // 아이템 태그 어뎁터 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        post_itemtag_rv.setLayoutManager(layoutManager);

        itemTagAdapter = new ItemTagAdapter(post_itemtag_rv.getContext(), itemTagData);
        post_itemtag_rv.setAdapter(itemTagAdapter);
        return true;
    }
    public boolean setCommentsData(){
        CommentsData = data.getAsJsonArray("comments");
        post_cmt_cnt.setText("댓글 " +CommentsData.size()+"");
        //코멘트 어뎁터 설정
        cmt_adapter = new PostCmtAdapter(post_cmt_list.getContext(), CommentsData );
        post_cmt_list.setAdapter(cmt_adapter);
        return true;
    }

}
