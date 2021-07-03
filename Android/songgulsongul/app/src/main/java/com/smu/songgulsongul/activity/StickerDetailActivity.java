package com.smu.songgulsongul.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.responseData.MarketDetailResponse;
import com.smu.songgulsongul.responseData.Sticker;
import com.smu.songgulsongul.responseData.User;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class StickerDetailActivity extends AppCompatActivity {
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    int sticker_id, price, user_point, user_id;
    List<Sticker> sticker;
    List<User> user;
    ImageView sticker_img, sticker_profile;
    TextView sticker_name, sticker_price, sticker_com, sticker_seller;
    String file_name, img_path, seller_id;
    final String DEFAULT_IMAGE = "/public/default/user.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_detail);

        sticker_img = (ImageView)findViewById(R.id.sticker_d_image);
        sticker_profile = (ImageView)findViewById(R.id.sticker_d_profile);
        sticker_name = (TextView)findViewById(R.id.sticker_d_name);
        sticker_price = (TextView)findViewById(R.id.sticker_d_price);
        sticker_com = (TextView)findViewById(R.id.sticker_d_comment);
        sticker_seller = (TextView)findViewById(R.id.sticker_d_seller);
        Button sticker_buy = (Button)findViewById(R.id.sticker_d_buy);

        user_id = LoginSharedPreference.getUserId(StickerDetailActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sticker_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        Intent intent = getIntent();
        sticker_id = intent.getIntExtra("sticker_id", -1);

        getStickerDtData();

        // 구매자 프로필 click listener
        sticker_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(StickerDetailActivity.this, ProfileActivity.class);
                intent1.putExtra("userId", seller_id);
                startActivity(intent1);
            }
        });

        // 구매 버튼 click listener
        sticker_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_point < price) { // 사용자 보유 포인트 부족시, 서버 통신 X
                    new AlertDialog.Builder(StickerDetailActivity.this)
                            .setMessage("포인트가 부족합니다." + "\n(보유 포인트: " + user_point + "p)")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(StickerDetailActivity.this)
                            .setMessage("이미지를 구매 할까요?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    serviceApi.StickerBuy(sticker_id, user_id).enqueue(new Callback<JsonObject>() {
                                        @Override
                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                            JsonObject result = response.body();
                                            int resultCode = result.get("code").getAsInt();

                                            if(resultCode == StatusCode.RESULT_OK){
                                                String image = result.get("image").getAsString();
                                                Log.d("image_path", image);

                                                // 이미지 저장
                                                file_name = img_path.substring(img_path.lastIndexOf('/') + 1, img_path.length());
                                                DownloadManager mgr = (DownloadManager)StickerDetailActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);

                                                Uri uri = Uri.parse(img_path);
                                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                                        .setAllowedOverRoaming(false)
                                                        .setTitle("MARKET IMAGE")
                                                        .setDescription("market image download..")
                                                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, file_name);
                                                mgr.enqueue(request);

                                                Toast.makeText(StickerDetailActivity.this, "구매 완료!", Toast.LENGTH_SHORT).show();

                                                // 포인트가 차감 되었으므로 새로고침
                                                Intent intent = getIntent();
                                                finish();
                                                startActivity(intent);
                                            }
                                            else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                                                new AlertDialog.Builder(StickerDetailActivity.this)
                                                        .setTitle("경고")
                                                        .setMessage("구매에 실패했습니다!"+"\n"+"다시 시도해주세요..")
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
                                            Toast.makeText(StickerDetailActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                            Log.e("스티커 구매 에러", t.getMessage());
                                            t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });

    }

    // 받아온 데이터로 셋팅
    public void setStickerData(){
        sticker_name.setText(sticker.get(0).getName());
        price = sticker.get(0).getPrice();
        sticker_price.setText(price + "p");


        img_path = sticker.get(0).getImage();
        Glide.with(this).load(img_path).into(sticker_img);
        sticker_com.append("\n\n\n");
        sticker_com.append(sticker.get(0).getText());

        String pro_img = user.get(0).getImg_profile();
        String img_addr;
        if(pro_img.equals(DEFAULT_IMAGE))
            img_addr = RetrofitClient.getBaseUrl() + pro_img;
        else
            img_addr = pro_img;
        Glide.with(this).load(img_addr).into(sticker_profile);

        seller_id = user.get(0).getLogin_id();
        sticker_seller.setText(seller_id);
    }

    //server에서 data전달
    public void getStickerDtData(){
        serviceApi.StickerDetail(sticker_id, user_id).enqueue(new Callback<MarketDetailResponse>() {
            @Override
            public void onResponse(Call<MarketDetailResponse> call, Response<MarketDetailResponse> response) {
                MarketDetailResponse result = response.body();
                int resultCode = result.getCode();

                if(resultCode == StatusCode.RESULT_OK){
                    sticker = result.getStickerDetail();
                    user = result.getSellerInfo();
                    user_point = result.getUserPoint();
                    setStickerData();
                }
                else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                    new AlertDialog.Builder(StickerDetailActivity.this)
                            .setTitle("경고")
                            .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 에러 발생 시 새로고침
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<MarketDetailResponse> call, Throwable t) {
                Toast.makeText(StickerDetailActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                Log.e("스티커 상세 보기에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

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