package smu.capstone.paper.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.HashTagAdapter;
import smu.capstone.paper.adapter.ItemTagAdapter;
import smu.capstone.paper.adapter.PostCmtAdapter;
import smu.capstone.paper.responseData.Ccl;
import smu.capstone.paper.responseData.CodeResponse;
import smu.capstone.paper.data.CommentData;
import smu.capstone.paper.responseData.Comment;
import smu.capstone.paper.responseData.HashTag;
import smu.capstone.paper.responseData.ItemTag;
import smu.capstone.paper.responseData.Post;
import smu.capstone.paper.responseData.PostDetail;
import smu.capstone.paper.responseData.PostResponse;
import smu.capstone.paper.responseData.User;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class PostActivity extends AppCompatActivity {
    ImageButton post_setting_btn, post_like_btn, post_keep_btn;
    RecyclerView post_hashtag_rv, post_itemtag_rv,post_cmt_list;


    EditText post_input;
    Button post_write;

    PostCmtAdapter cmt_adapter;
    ItemTagAdapter itemTagAdapter;
    HashTagAdapter hashTagAdapter;

    TextView post_user_id, post_like_cnt, post_cmt_cnt, post_text, post_date;
    ImageView post_pic, post_profile, post_ccl_cc, post_ccl_a, post_ccl_nc, post_ccl_nd, post_ccl_sa;


    Date today = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    Calendar rightNow = Calendar.getInstance();

    int status;
    final int MY = 1;
    final int OTHER = 2;


    int user_id, post_id;
    String login_id, p_user_id, img_path;
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;

    PostDetail data;
    User userData;
    Post postData;
    List<HashTag> hashTagsData;
    List<ItemTag> itemTagData;
    List<Comment> CommentsData;
    Ccl ccl;


    Typeface typeface;
    Balloon  balloon, balloon2, balloon3, balloon4, balloon5;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        {
            typeface = ResourcesCompat.getFont(this, R.font.ibm_plex_sans_light);
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
            post_cmt_list =  findViewById(R.id.post_cmt_list);
            post_hashtag_rv = findViewById(R.id.post_hashtag_rv);

            Typeface tf = ResourcesCompat.getFont(this, R.font.ibm_plex_sans_light);
            post_input.setTypeface(tf);

        }

        //툴바 세팅
        Toolbar toolbar = (Toolbar)findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setTitle("세부 게시글");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        Intent intent = getIntent();
        user_id = LoginSharedPreference.getUserId(PostActivity.this);
        post_id = intent.getIntExtra("post_id",-1);
        login_id = LoginSharedPreference.getLoginId(PostActivity.this);


        //서버통신
        getData();

        post_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goZoom = new Intent(PostActivity.this , ZoomActivity.class);
                goZoom.putExtra("path", RetrofitClient.getBaseUrl() + postData.getImage() );
                startActivity(goZoom);
            }
        });

        post_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(),v);
                Log.d("p_user_id", p_user_id);
                //if(login_id.equals(p_user_id))
                    popup.getMenuInflater().inflate(R.menu.post_setting_menu, popup.getMenu());
                //else
                //    popup.getMenuInflater().inflate(R.menu.post_setting_menu_other, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item){
                        switch(item.getItemId()){
                            case R.id.post_edit:
                                Intent intent = new Intent(PostActivity.this, PostEditActivity.class);
                                intent.putExtra("postID", postData.getId());
                                startActivity(intent);
                                finish();

                                break;
                            case R.id.post_save:
                                Intent intent2 = new Intent(PostActivity.this, SaveImageActivity.class);
                                intent2.putExtra("post_id", post_id);
                                intent2.putExtra("img_path", img_path);

                                startActivity(intent2);
                                break;
                            case R.id.post_delete:
                                new AlertDialog.Builder(PostActivity.this)
                                        .setMessage("게시물을 삭제 하시겠습니까?")
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                serviceApi.PostDelete(user_id, post_id).enqueue(new Callback<CodeResponse>() {
                                                    @Override
                                                    public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                                        try {
                                                            CodeResponse result = response.body();
                                                            int resultCode = result.getCode();

                                                            if (resultCode == StatusCode.RESULT_OK) {
                                                                Toast.makeText(getApplicationContext(), "게시글 삭제 완료!", Toast.LENGTH_SHORT).show();
                                                                onBackPressed();
                                                                finish();
                                                            } else if (resultCode == StatusCode.RESULT_SERVER_ERR) {
                                                                Toast.makeText(getApplicationContext(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (NullPointerException e){
                                                            new AlertDialog.Builder(PostActivity.this)
                                                                    .setMessage("에러발생!")
                                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                        }
                                                                    })
                                                                    .show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<CodeResponse> call, Throwable t) {
                                                        Toast.makeText(PostActivity.this,  "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                                    }
                                                });
                                            }
                                        })
                                        .show();
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
                serviceApi.Like(LoginSharedPreference.getUserId(PostActivity.this),
                        postData.getId() ).enqueue(new Callback<CodeResponse>() {
                    @Override
                    public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                        int like = data.getLikeOnset();
                        int resultCode = response.body().getCode();
                        if( resultCode == statusCode.RESULT_OK){

                            int likeNum = data.getLikeNum();

                            if( like == 1){ //좋아요 취소하기
                                like = 0;
                                data.setLikeNum(--likeNum);
                            }
                            else{
                                like = 1;
                                data.setLikeNum(++likeNum);
                            }
                            data.setLikeOnset(like);
                            post_like_cnt.setText("좋아요 " + likeNum);
                            post_like_btn.setSelected(!post_like_btn.isSelected()); //버튼 반대로 체크
                        }
                        else if( resultCode == statusCode.RESULT_CLIENT_ERR){
                            Toast.makeText(PostActivity.this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if( resultCode == statusCode.RESULT_SERVER_ERR){
                            Toast.makeText(PostActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CodeResponse> call, Throwable t) {
                        Toast.makeText(PostActivity.this,  "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
            }
        });

        post_keep_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                serviceApi.Keep(LoginSharedPreference.getUserId(PostActivity.this),postData.getId())
                        .enqueue(new Callback<CodeResponse>() {
                            @Override
                            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                int keep = data.getKeepOnset();
                                int resultCode = response.body().getCode();
                                if( resultCode == statusCode.RESULT_OK){
                                    keep = (keep==1)? 0 : 1;
                                    data.setKeepOnset(keep);
                                    if( keep == 1)
                                        Toast.makeText(PostActivity.this, "보관함에 저장 되었습니다", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(PostActivity.this, "보관함에서 삭제 되었습니다", Toast.LENGTH_SHORT).show();


                                    post_keep_btn.setSelected(!post_keep_btn.isSelected());

                                }
                                else if( resultCode == statusCode.RESULT_CLIENT_ERR){
                                    Toast.makeText(PostActivity.this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                                }
                                else if( resultCode == statusCode.RESULT_SERVER_ERR){
                                    Toast.makeText(PostActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CodeResponse> call, Throwable t) {
                                Toast.makeText(PostActivity.this,  "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                            }
                        });

            }
        });

        // 텍스트 입력시 댓글작성 버튼 활성화
        post_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    post_write.setClickable(true);
                    post_write.setTextColor(R.color.inkGrey);
                    post_write.setTextSize(16);
                    SpannableString spanString = new SpannableString("게시");
                    spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                    post_write.setText(spanString);

                }
                else{
                    post_write.setClickable(false);
                    post_write.setTextColor(R.color.inkGreyTransparent);
                    post_write.setTextSize(14);
                    SpannableString spanString = new SpannableString("게시");
                    spanString.setSpan(new StyleSpan(Typeface.NORMAL), 0, spanString.length(), 0);
                    post_write.setText(spanString);

                }
            }
        });
        //댓글 작성 기능
        post_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //댓글 작성!
                serviceApi.Comment(new CommentData(
                        LoginSharedPreference.getUserId(PostActivity.this),
                        postData.getId(),
                        post_input.getText().toString()
                )).enqueue(new Callback<CodeResponse>() {
                    @Override
                    public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                        int resultCode = response.body().getCode();
                        if( resultCode == statusCode.RESULT_OK){
                             //다시 불러오기.. 아니면 댓글만 가져오는 코드 짜야함!
                            post_input.setText("");
                            getData();
                        }
                        else if( resultCode == statusCode.RESULT_CLIENT_ERR){
                            Toast.makeText(PostActivity.this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if( resultCode == statusCode.RESULT_SERVER_ERR){
                            Toast.makeText(PostActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CodeResponse> call, Throwable t) {
                        Toast.makeText(PostActivity.this,  "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });


            }
        });

        View.OnClickListener goProfile = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, ProfileActivity.class);
                // 게시글 사용자 id 전달
                intent.putExtra("userId", post_user_id.getText());
                startActivity(intent);
            }
        };

        post_user_id.setOnClickListener(goProfile);
        post_profile.setOnClickListener(goProfile);

        // ccl 설명 Ballon 세팅
        setCCL_Ballons();
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
        serviceApi.GetDetailPost(post_id, user_id).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                PostResponse result = response.body();

                int resultCode = result.getCode();

                if( resultCode == statusCode.RESULT_OK){
                    data = result.getData();
                    setPostData();
                    setHashTagData();
                    setItemTagData();
                    setCommentsData();
                    setStatusData();

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
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(PostActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });


    }
    public void setCCL_Ballons(){
        balloon = new Balloon.Builder(getApplicationContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(0.5f)
                .setWidthRatio(0.2f)
                .setTextSize(10f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("공유 허용")
                .setTextTypeface(typeface)
                .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inkGrey))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        post_ccl_cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon.showAlignBottom(post_ccl_cc);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        balloon.dismiss();
                    }
                }, 2000);
            }
        });

        balloon2 = new Balloon.Builder(getApplicationContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(0.5f)
                .setWidthRatio(0.2f)
                .setTextSize(10f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("동일 조건 변경 허용")
                .setTextTypeface(typeface)
                .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inkGrey))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        post_ccl_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon2.showAlignBottom(post_ccl_a);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        balloon2.dismiss();
                    }
                }, 2000);
            }
        });

        balloon3 = new Balloon.Builder(getApplicationContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(0.5f)
                .setWidthRatio(0.2f)
                .setTextSize(10f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("상업적 이용 비허용")
                .setTextTypeface(typeface)
                .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inkGrey))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        post_ccl_nc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon3.showAlignBottom(post_ccl_nc);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        balloon3.dismiss();
                    }
                }, 2000);
            }
        });

        balloon4 = new Balloon.Builder(getApplicationContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(0.5f)
                .setWidthRatio(0.2f)
                .setTextSize(10f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("2차 변경 비허용")
                .setTextTypeface(typeface)
                .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inkGrey))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        post_ccl_nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon4.showAlignBottom(post_ccl_nd);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        balloon4.dismiss();
                    }
                }, 2000);
            }
        });

        balloon5 = new Balloon.Builder(getApplicationContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(0.5f)
                .setWidthRatio(0.2f)
                .setTextSize(10f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("출처 표기")
                .setTextTypeface(typeface)
                .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inkGrey))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        post_ccl_sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon5.showAlignBottom(post_ccl_sa);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        balloon5.dismiss();
                    }
                }, 2000);
            }
        });
    }
    public boolean setPostData(){
        postData = data.getPost();
        userData = data.getUser();
        ccl = postData.getCcl();

        //작성자 프로필
        p_user_id = userData.getLogin_id();
        post_user_id.setText(userData.getLogin_id());
        img_path = userData.getImg_profile();
        Glide.with(this).load(RetrofitClient.getBaseUrl() + img_path).into(post_profile);

        // 게시글 정보 세팅
        if( date.format(today).equals((postData.getPost_date()))) {
            int hour = Integer.parseInt(postData.getPost_time().substring(0,2));
            if( hour != rightNow.get(Calendar.HOUR_OF_DAY) ){
                post_date.setText((rightNow.get(Calendar.HOUR_OF_DAY) - hour)+"시간 전");
            }
            else {
                int min = Integer.parseInt(postData.getPost_time().substring(3,5));
                if( min == rightNow.get(Calendar.MINUTE) )
                    post_date.setText("방금 게시됨");
                else
                    post_date.setText((rightNow.get(Calendar.MINUTE) - min) + "분 전");
            }
        }
        else
            post_date.setText( postData.getPost_date()); //게시 날짜

        post_text.setText(postData.getText());
        Glide.with(this).load(RetrofitClient.getBaseUrl() + postData.getImage()).into(post_pic);


        // data 세팅
        post_like_cnt.setText("좋아요 " + data.getLikeNum());
        if( data.getLikeOnset() == 1)
            post_like_btn.setSelected(true);
        else
            post_like_btn.setSelected(false);

        if( data.getKeepOnset() == 1)
            post_keep_btn.setSelected(true);
        else
            post_keep_btn.setSelected(false);




        if(ccl.getCcl_cc()==1)
            post_ccl_cc.setImageResource(R.drawable.ccl_cc_fill);
        if(ccl.getCcl_a()==1)
            post_ccl_a.setImageResource(R.drawable.ccl_attribution_fill);
        if(ccl.getCcl_nc()==1)
            post_ccl_nc.setImageResource(R.drawable.ccl_noncommercial_fill);
        if(ccl.getCcl_nd()==1)
            post_ccl_nd.setImageResource(R.drawable.ccl_no_derivative_fill);
        if(ccl.getCcl_sa()==1)
            post_ccl_sa.setImageResource(R.drawable.ccl_share_alike_fill);


        return true;
    }
    public boolean setHashTagData(){

        hashTagsData = data.getHashTags();
        //해쉬 태그 어뎁터 설정
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        post_hashtag_rv.setLayoutManager(layoutManager2);

        hashTagAdapter = new HashTagAdapter(post_hashtag_rv.getContext(), hashTagsData);
        post_hashtag_rv.setAdapter(hashTagAdapter);
        return true;
    }
    public boolean setItemTagData(){
        itemTagData = data.getItemTags();
        // 아이템 태그 어뎁터 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        post_itemtag_rv.setLayoutManager(layoutManager);

        itemTagAdapter = new ItemTagAdapter(post_itemtag_rv.getContext(), itemTagData);
        post_itemtag_rv.setAdapter(itemTagAdapter);
        return true;
    }
    public boolean setCommentsData(){
        CommentsData = data.getComments();
        post_cmt_cnt.setText("댓글 " +CommentsData.size()+"");
        //코멘트 어뎁터 설정

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        post_cmt_list.setLayoutManager(layoutManager);

        cmt_adapter = new PostCmtAdapter(post_cmt_list.getContext(), CommentsData );
        cmt_adapter.setOnItemLongClickListener(new PostCmtAdapter.OnItemLongClickEventListener() {
            @Override
            public void onItemLongClick(View a_view, final int a_position) {
                Log.d("comment", "long click!" +  CommentsData.get(a_position).getUser_id() + " : "
                        + LoginSharedPreference.getUserId(PostActivity.this) );
                if( CommentsData.get(a_position).getUser_id() == LoginSharedPreference.getUserId(PostActivity.this) ){
                    //댓글 삭제 알림 팝업
                    Log.d("comment", "삭제해보자요");
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle("경고")
                            .setMessage("댓글을 삭제하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteComment(CommentsData.get(a_position).getId());
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }
                            )
                            .show();
                }
            }
        });


        post_cmt_list.setAdapter(cmt_adapter);

        return true;
    }
    public boolean setStatusData(){
        if(LoginSharedPreference.getLoginId(PostActivity.this).equals( userData.getLogin_id()) )
            status = MY;
        else
            status = OTHER ;

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
        return true;
    }
    public void deleteComment(int id){
        serviceApi.DeleteComment(post_id,id).enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                int resultCode = response.body().getCode();
                if( resultCode == statusCode.RESULT_OK){
                    //다시 불러오기.. 아니면 댓글만 가져오는 코드 짜야함!
                    Toast.makeText(PostActivity.this, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    getData();
                }
                else if( resultCode == statusCode.RESULT_CLIENT_ERR){
                    Toast.makeText(PostActivity.this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                }
                else if( resultCode == statusCode.RESULT_SERVER_ERR){
                    Toast.makeText(PostActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                Toast.makeText(PostActivity.this,  "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
    }

}
