package com.smu.songgulsongul.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.adapter.ItemSearchAdapter;
import com.smu.songgulsongul.item.ItemSearchItem;
import com.smu.songgulsongul.responseData.ItemTag;


public class AddItemtagActivity extends Activity {
    SearchView searchView;


    StringBuffer response;
    String apiHtml;

    RecyclerView mRecyclerView = null;
    ItemSearchAdapter mAdapter = null;
    private ArrayList<ItemSearchItem> mlist = new ArrayList<ItemSearchItem>();
    int position = -1;

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Bundle bun = msg.getData();
            apiHtml = bun.getString("API_HTML");

            //부호 정리
            apiHtml = apiHtml.replaceAll("<b>","");
            apiHtml = apiHtml.replaceAll("</b>","");
            apiHtml = apiHtml.replaceAll("&lt;","<");
            apiHtml = apiHtml.replaceAll("&gt;",">");
            apiHtml = apiHtml.replaceAll("&amp;","&");

            try{
                JSONObject jsonObject = new JSONObject(apiHtml);
                JSONArray apiArray = jsonObject.getJSONArray("items");

                mlist.clear();

                for (int i = 0; i<apiArray.length();i++){
                    JSONObject apiObject = apiArray.getJSONObject(i);
                    ItemSearchItem item = new ItemSearchItem();

                    //item에 JSONObject 값 전달
                    item.setId(apiObject.getInt("productId"));
                    item.setPic(apiObject.getString("image"));
                    item.setName(apiObject.getString("title"));
                    item.setHprice(apiObject.getString("hprice"));
                    item.setLprice(apiObject.getString("lprice"));
                    item.setUrl(apiObject.getString("link"));
                    item.setBrand(apiObject.getString("brand"));
                    item.setCategory1(apiObject.getString("category3"));
                    item.setCategory2(apiObject.getString("category4"));

                    mlist.add(item);
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public static StringBuilder sb;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_itemtag);

        searchView = findViewById(R.id.itemtag_search);
        mRecyclerView = findViewById(R.id.itemtag_list);

        mAdapter = new ItemSearchAdapter(mlist);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();

        //검색창 전체 영역 터치 가능
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        //검색창 리스너
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ApiSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //아이템 클릭 리스너
        mAdapter.setOnItemClickListener(new ItemSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                position = pos;
                Intent intent = getIntent();

                resultData();
            }
        });

        //윈도우 크기 설정
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int)(display.getWidth()* 0.98);
        int height = (int)(display.getHeight() * 0.9);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }

    private void resultData(){
        Intent intent = new Intent();

        ItemSearchItem item = mlist.get(position);

        //UploadDetailActivity로 보낼 데이터
        intent.putExtra("id",item.getId());
        intent.putExtra("name", item.getName());
        intent.putExtra("hprice", item.getHprice());
        intent.putExtra("lprice", item.getLprice());
        intent.putExtra("url", item.getUrl());
        intent.putExtra("picture", item.getPic());
        intent.putExtra("brand", item.getBrand());
        intent.putExtra("category1", item.getCategory1());
        intent.putExtra("category2", item.getCategory2());

        setResult(RESULT_OK,intent);
        finish();
    }

    // server에서 data전달
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    public void ApiSearch(final String keyword){
        if(keyword.equals("")&&keyword != null){
            Toast.makeText(getApplicationContext(),"검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        else{
            new Thread(){
                @Override
                public void run() {
                    String clientId = "vzYQ1acA6vGEyvjeQHAB";// 애플리케이션 클라이언트 아이디값";
                    String clientSecret = "cg4YKUanSV";// 애플리케이션 클라이언트 시크릿값";\
                    int display = 20; // 검색결과갯수

                    try {
                        String text = URLEncoder.encode(keyword, "utf-8");
                        String apiURL = "https://openapi.naver.com/v1/search/shop.json?query=" + text + "&display=" + display + "&";

                        URL url = new URL(apiURL);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("X-Naver-Client-Id", clientId);
                        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                        BufferedReader br;
                        int responseCode = con.getResponseCode();

                        if (responseCode == 200) {
                            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                        } else {
                            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        }
                        String inputLine;
                        response = new StringBuffer();

                        while ((inputLine = br.readLine()) != null) {
                            response.append(inputLine);
                            response.append("\n");
                        }
                        br.close();

                        String apiHtml = response.toString();

                        Bundle bun = new Bundle();
                        bun.putString("API_HTML", apiHtml);
                        Message msg = handler.obtainMessage();
                        msg.setData(bun);
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
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
