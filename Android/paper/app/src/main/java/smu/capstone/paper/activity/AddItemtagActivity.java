package smu.capstone.paper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
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

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.HomeMarketAdapter;
import smu.capstone.paper.adapter.ItemSearchAdapter;
import smu.capstone.paper.item.ItemSearchItem;
import smu.capstone.paper.item.ItemtagItem;


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
                    item.setPic(apiObject.getString("image"));
                    item.setName(apiObject.getString("title"));
                    item.setHprice(apiObject.getString("hprice"));
                    item.setLprice(apiObject.getString("lprice"));

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
                String mdata = intent.getStringExtra("key");

                System.out.println(mdata); //null 나옴

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

        intent.putExtra("key", mlist.get(position).toString()); //맞는 position의 주소값이 제대로 나오긴 함

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
                    int display = 5; // 검색결과갯수

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

    public void RunApi(String keyword){
        String clientId = "vzYQ1acA6vGEyvjeQHAB";// 애플리케이션 클라이언트 아이디값";
        String clientSecret = "cg4YKUanSV";// 애플리케이션 클라이언트 시크릿값";\
        int display = 2; // 검색결과갯수. 최대100개
        try {
            String text = URLEncoder.encode(keyword, "utf-8");
            String apiURL = "https://openapi.naver.com/v1/search/shop.json?query=" + text + "&display=" + display + "&";

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            con.disconnect();

            String data = sb.toString();
            String[] array;
            array = data.split("\"");

            String[] title = new String[display];
            String[] image = new String[display];
            String[] lprice = new String[display];
            String[] hprice = new String[display];
            String[] productId = new String[display];

            int k = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals("title"))
                    title[k] = array[i + 2];
                if (array[i].equals("image"))
                    image[k] = array[i + 2];
                if (array[i].equals("lprice"))
                    lprice[k] = array[i + 2];
                if (array[i].equals("hprice"))
                    hprice[k] = array[i + 2];
                if (array[i].equals("productId")) {
                    productId[k] = array[i + 2];
                    k++;
                }
            }
            System.out.println(sb);

            for(int j = 0; j<title.length; j++) {
                System.out.println(title[j]);
                System.out.println(image[j]);
                System.out.println(lprice[j]);
                System.out.println(hprice[j]);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
