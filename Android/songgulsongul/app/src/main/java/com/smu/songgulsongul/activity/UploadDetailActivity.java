package com.smu.songgulsongul.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.SettingSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.adapter.AddItemTagAdapter;
import com.smu.songgulsongul.adapter.ItemTagAdapter;

import com.smu.songgulsongul.responseData.ItemTag;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class UploadDetailActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    String filePath;
    int user_id;
    RecyclerView itemtag_rv;
    AddItemTagAdapter adapter;
    List<ItemTag> itemTagData = new ArrayList<>();
    EditText hashtagText, uploadText;
    SwitchCompat upload_detail_ccl_1, upload_detail_ccl_2, upload_detail_ccl_3 , upload_detail_ccl_4, upload_detail_ccl_5;
    ImageView upload_detail_img;
    int ccl1, ccl2, ccl3, ccl4, ccl5;

    RequestBody requestFile, requestId, requestText;
    MultipartBody.Part imageBody;
    ArrayList<MultipartBody.Part> hash_tags = new ArrayList<>();
    ArrayList<MultipartBody.Part> ccl_list = new ArrayList<>();
    ArrayList<MultipartBody.Part> item_tags = new ArrayList<>();

    final int ON = 1;
    final int OFF = 0;

    long first_time = 0;
    long second_time = 0;

    int BackColor = Color.parseColor("#BFB1D8");
    int FontColor = Color.parseColor("#000000");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_detail);

        uploadText = findViewById(R.id.upload_text);
        user_id = LoginSharedPreference.getUserId(this);


        //이미지 세팅
        filePath = getIntent().getStringExtra("path");
        upload_detail_img = findViewById(R.id.upload_detail_pic);
        Glide.with(this).load(filePath).into(upload_detail_img);

        //ccl 세팅
        upload_detail_ccl_1 = findViewById(R.id.upload_detail_ccl_1);
        upload_detail_ccl_2 = findViewById(R.id.upload_detail_ccl_2);
        upload_detail_ccl_3 = findViewById(R.id.upload_detail_ccl_3);
        upload_detail_ccl_4 = findViewById(R.id.upload_detail_ccl_4);
        upload_detail_ccl_5 = findViewById(R.id.upload_detail_ccl_5);

        upload_detail_ccl_1.setChecked(SettingSharedPreference.getSetting(this,"ccl1"));
        upload_detail_ccl_2.setChecked(SettingSharedPreference.getSetting(this,"ccl2"));
        upload_detail_ccl_3.setChecked(SettingSharedPreference.getSetting(this,"ccl3"));
        upload_detail_ccl_4.setChecked(SettingSharedPreference.getSetting(this,"ccl4"));
        upload_detail_ccl_5.setChecked(SettingSharedPreference.getSetting(this,"ccl5"));

        // 툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.upload_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        //해쉬태그 작성
        hashtagText = findViewById(R.id.upload_hashtag);
        hashtagText.append("#");
        hashtagText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if(source.charAt(i) == ' ')
                        return " #";
                    else if(source.charAt(i) == '#')
                        continue;
                    else if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        }});

        hashtagText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG", charSequence.length() + ":" + charSequence+"");
                if(charSequence.length()==0)
                    hashtagText.append("#");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // 아이템 태그 어뎁터 설정
        itemtag_rv = findViewById(R.id.upload_itemtag_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        itemtag_rv.setLayoutManager(layoutManager);


        itemTagData.add(new ItemTag(-1));
        adapter = new AddItemTagAdapter(itemtag_rv.getContext(),itemTagData ); // 추가모드 어뎁터 세팅

        // 적용
        itemtag_rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new AddItemTagAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                Intent intent = new Intent(UploadDetailActivity.this, AddItemtagActivity.class);

                startActivityForResult(intent,1234);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234 && resultCode == RESULT_OK){
            //AddItemtagActivity에서 받아온 데이터 처리
            String name = data.getStringExtra("name");
            String hprice = data.getStringExtra("hprice");
            String lprice = data.getStringExtra("lprice");
            String url = data.getStringExtra("url");
            String picture = data.getStringExtra("picture");
            String brand = data.getStringExtra("brand");
            String category1 = data.getStringExtra("category1");
            String category2 = data.getStringExtra("category2");
            Log.d("TAG", name+" " + hprice+" " + lprice+ url+" " + picture+ " " +brand+ " " +category1+ " " +category2);
            itemTagData.add(new ItemTag( name, hprice, lprice, url, picture, brand, category1, category2));
            adapter.notifyItemChanged(itemTagData.size()-1);
        }
        else{
            System.out.println("there is no data");
        }
    }

    // 서버에 전송할 데이터 묶기
    public void makeUploadData(){
        hash_tags.clear();
        ccl_list.clear();
        item_tags.clear();
        ccl1 = ccl2 = ccl3 = ccl4 = ccl5 = OFF;

        // 업로드 이미지
        File file = new File(filePath);
        requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        imageBody = MultipartBody.Part.createFormData("img_post", file.getName(), requestFile);

        // 사용자 아이디
        requestId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id));

        // 텍스트
        String text = uploadText.getText().toString().trim();
        requestText = RequestBody.create(MediaType.parse("text/plain"), text);

        // 해쉬태그
        String hashTag = hashtagText.getText().toString() + " ";
        Pattern pattern = Pattern.compile("[#](.*?)[ ]");
        Matcher matcher = pattern.matcher(hashTag);
        while(matcher.find()){
            hash_tags.add(MultipartBody.Part.createFormData("hash_tag", matcher.group(1)));

            if(matcher.group(1) == null)
                break;
        }

        // ccl
        if(upload_detail_ccl_1.isChecked())
            ccl1 = ON;
        if(upload_detail_ccl_2.isChecked())
            ccl2 = ON;
        if(upload_detail_ccl_3.isChecked())
            ccl3 = ON;
        if(upload_detail_ccl_4.isChecked())
            ccl4 = ON;
        if(upload_detail_ccl_5.isChecked())
            ccl5 = ON;

        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl1)));
        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl2)));
        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl3)));
        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl4)));
        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl5)));

        // 아이템 태그
        List<ItemTag> items = adapter.getDataList();
        Log.d("item_count", String.valueOf(items.size()));

        if(items.size() > 1) {
            for (int i = 1; i < items.size(); i++) {
                item_tags.add(MultipartBody.Part.createFormData("item_name",
                        items.get(i).getName() == null ? "" : items.get(i).getName()));
                item_tags.add(MultipartBody.Part.createFormData("item_lowprice",
                        String.valueOf(items.get(i).getL_price())));
                item_tags.add(MultipartBody.Part.createFormData("item_highprice",
                        String.valueOf(items.get(i).getH_price())));
                item_tags.add(MultipartBody.Part.createFormData("item_link",
                        items.get(i).getUrl() == null ? "" : items.get(i).getUrl()));
                item_tags.add(MultipartBody.Part.createFormData("item_img",
                        items.get(i).getPicture() == null ? "" : items.get(i).getPicture()));
                item_tags.add(MultipartBody.Part.createFormData("item_brand",
                        items.get(i).getBrand() == null ? "" :  items.get(i).getBrand()));
                item_tags.add(MultipartBody.Part.createFormData("item_category1",
                        items.get(i).getCategory1() == null ? "" : items.get(i).getCategory1()));
                item_tags.add(MultipartBody.Part.createFormData("item_category2",
                        items.get(i).getCategory2() == null ? "" : items.get(i).getCategory2()));
            }
        }
    }

    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if(second_time-first_time <2000){
            super.onBackPressed();
            finish();
        }
        else{
            Toasty.custom(this, "한번 더 누르면 업로드를 종료합니다", null, BackColor, FontColor, 2000, false, true).show();
            first_time = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
               // 알림 팝업
                return true;

            case R.id.toolbar_done :
                makeUploadData();
                serviceApi.PostUpload(requestId, requestText, hash_tags, ccl_list, item_tags, imageBody).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            JsonObject result = response.body();
                            int resultCode = result.get("code").getAsInt();
                            int post_id = result.get("post_id").getAsInt();

                            if (resultCode == StatusCode.RESULT_OK) {
                                Toast toast = Toasty.custom(UploadDetailActivity.this, "업로드 완료", null, BackColor, FontColor, 2000, false, true);
                                toast.show();
                                Intent intent = new Intent(UploadDetailActivity.this, PostActivity.class); // 업로드 된 게시물로 이동 (게시글 id 넘기기)
                                intent.putExtra("post_id", post_id);
                                startActivity(intent);
                                finish();
                            } else if (resultCode == StatusCode.RESULT_SERVER_ERR) {
                                new AlertDialog.Builder(UploadDetailActivity.this)
                                        .setMessage("업로드에 실패했습니다." + "\n" + "다시 시도해주세요..")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                            }
                        } catch (NullPointerException e){
                            new AlertDialog.Builder(UploadDetailActivity.this)
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
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toasty.normal(UploadDetailActivity.this, "서버와의 통신이 불안정합니다").show();
                        Log.e("게시글 업로드 에러", t.getMessage());
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
                return true;

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