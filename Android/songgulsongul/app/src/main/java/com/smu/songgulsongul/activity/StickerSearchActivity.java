package com.smu.songgulsongul.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.adapter.StickerSearchAdapter;
import com.smu.songgulsongul.responseData.MarketResponse;
import com.smu.songgulsongul.responseData.Sticker;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class StickerSearchActivity extends AppCompatActivity {
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StickerSearchAdapter adapter;
    RecyclerView recyclerView;
    TextView sticker_sort_price;
    TextView sticker_sort_data;
    String keyword;

    List<Sticker> stickers;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_search);

        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        sticker_sort_price = (TextView)findViewById(R.id.sticker_sort_price);
        sticker_sort_data = (TextView)findViewById(R.id.sticker_sort_date);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sticker_search_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(keyword);

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        // 낮은 가격순 정렬
        sticker_sort_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceApi.SearchPrice(keyword, 0).enqueue(new Callback<MarketResponse>() {
                    @Override
                    public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
                        MarketResponse result = response.body();
                        int resultCode = result.getCode();

                        if(resultCode == StatusCode.RESULT_OK){
                            stickers = result.getMarketItem();
                            adapter = new StickerSearchAdapter(StickerSearchActivity.this, stickers);
                            recyclerView.setAdapter(adapter);
                        }

                        else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                            Toasty.normal(StickerSearchActivity.this, "서버와의 통신이 불안정합니다").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MarketResponse> call, Throwable t) {
                        Toasty.normal(StickerSearchActivity.this, "서버와의 통신이 불안정합니다").show();
                        Log.e("낮은 가격순 정렬 에러", t.getMessage());
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
            }
        });

        // 최신순 정렬
        sticker_sort_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceApi.SearchDate(keyword, 0).enqueue(new Callback<MarketResponse>() {
                    @Override
                    public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
                        MarketResponse result = response.body();
                        int resultCode = result.getCode();

                        if(resultCode == StatusCode.RESULT_OK){
                            stickers = result.getMarketItem();
                            adapter = new StickerSearchAdapter(StickerSearchActivity.this, stickers);
                            recyclerView.setAdapter(adapter);
                        }

                        else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                            Toasty.normal(StickerSearchActivity.this, "서버와의 통신이 불안정합니다").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MarketResponse> call, Throwable t) {
                        Toasty.normal(StickerSearchActivity.this, "서버와의 통신이 불안정합니다").show();
                        Log.e("최신순 정렬 에러", t.getMessage());
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
            }
        });

        recyclerView = findViewById(R.id.sticker_search_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getStickerData();
    }

    public void getStickerData(){
        serviceApi.StickerSearch(keyword, 0).enqueue(new Callback<MarketResponse>() {
            @Override
            public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
                MarketResponse result = response.body();
                int resultCode = result.getCode();

                if(resultCode == StatusCode.RESULT_OK){
                    stickers = result.getMarketItem();
                    adapter = new StickerSearchAdapter(StickerSearchActivity.this, stickers);
                    recyclerView.setAdapter(adapter);
                }

                else if(resultCode == StatusCode.RESULT_SERVER_ERR){

                    View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(StickerSearchActivity.this);
                    builder.setView(dialogView);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ImageView icon=dialogView.findViewById(R.id.warning);
                    icon.setVisibility(View.GONE);

                    TextView txt=dialogView.findViewById(R.id.txtText);
                    txt.setText("서버 에러!"+"\n"+"다시 시도해주세요.");

                    Button ok_btn = dialogView.findViewById(R.id.okBtn);
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //화면 새로고침
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });

                    Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                    cancel_btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MarketResponse> call, Throwable t) {
                Toasty.normal(StickerSearchActivity.this, "서버와의 통신이 불안정합니다").show();
                Log.e("스티커 불러오기 에러", t.getMessage());
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