package com.smu.songgulsongul.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.recycler_adapter.ItemSearchAdapter;
import com.smu.songgulsongul.data.shoppingapi.ShoppingResults;
import com.smu.songgulsongul.recycler_item.ItemSearchItem;
import com.smu.songgulsongul.server.NaverApi;
import com.smu.songgulsongul.server.RetrofitNaver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddItemtagActivity extends Activity {

    SearchView searchView;
    RecyclerView recyclerView = null;
    ItemSearchAdapter adapter = null;

    private final ArrayList<ItemSearchItem> mlist = new ArrayList<ItemSearchItem>();

    int position = -1;
    String key;

    Button more;
    boolean isLoading;


    NaverApi naverApi = RetrofitNaver.getClient().create(NaverApi.class);

    private final static String CLIENT_ID = "vzYQ1acA6vGEyvjeQHAB";
    private final static String CLIENT_PW = "cg4YKUanSV";


    int idx = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_itemtag);

        searchView = findViewById(R.id.itemtag_search);
        recyclerView = findViewById(R.id.itemtag_list);


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
                SearchRetrofit(query);
                key = query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //윈도우 크기 설정
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.98);
        int height = (int) (display.getHeight() * 0.98);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

    }

    private void resultData() {
        Intent intent = new Intent();

        ItemSearchItem item = mlist.get(position);

        //UploadDetailActivity로 보낼 데이터
        String name = item.getTitle();
        name = name.replaceAll("<b>", "");
        name = name.replaceAll("<b>", "");
        name = name.replaceAll("</b>", "");
        name = name.replaceAll("&lt;", "<");
        name = name.replaceAll("&gt;", ">");
        name = name.replaceAll("&amp;", "&");

        intent.putExtra("name", name);
        intent.putExtra("hprice", item.getHprice());
        intent.putExtra("lprice", item.getLprice());
        intent.putExtra("url", item.getLink());
        intent.putExtra("picture", item.getImg());
        intent.putExtra("brand", item.getBrand());
        intent.putExtra("category1", item.getCategory3());
        intent.putExtra("category2", item.getCategory4());

        setResult(RESULT_OK, intent);
        finish();
    }

    // server에서 data전달
    public void SearchRetrofit(final String keyword) {
        naverApi.search(CLIENT_ID, CLIENT_PW, keyword, 10, 1).enqueue(new Callback<ShoppingResults>() {
            @Override
            public void onResponse(Call<ShoppingResults> call, Response<ShoppingResults> response) {
                Log.d("item", response.toString());

                ShoppingResults result = response.body();
                if (response.isSuccessful()) {
                    //리스트 비우고 상태 초기화
                    mlist.clear();

                    // 데이터 세팅
                    mlist.addAll(result.getItems());

                    //어뎁터 초기화
                    adapter = new ItemSearchAdapter(AddItemtagActivity.this, mlist);
                    adapter.setOnItemClickListener(new ItemSearchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int pos) {
                            position = pos;
                            Intent intent = getIntent();
                            resultData();
                        }
                    });

                    // 어뎁터 세팅
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AddItemtagActivity.this));

                    idx += 10;
                    initScroll();

                } else {

                }
            }

            @Override
            public void onFailure(Call<ShoppingResults> call, Throwable t) {
            }
        });
    }

    public void SearchRetrofitMore(final String keyword) {
        mlist.add(null);
        adapter.notifyItemInserted(mlist.size() - 1);

        naverApi.search(CLIENT_ID, CLIENT_PW, keyword, 10, idx).enqueue(new Callback<ShoppingResults>() {
            @Override
            public void onResponse(Call<ShoppingResults> call, Response<ShoppingResults> response) {
                mlist.remove(mlist.size() - 1);
                adapter.notifyItemRemoved(mlist.size());

                ShoppingResults result = response.body();
                if (response.isSuccessful()) {
                    //데이터 추가!
                    mlist.addAll(result.getItems());
                    adapter.notifyDataSetChanged();
                    idx += 10;

                } else {

                }


                isLoading = false;

            }

            @Override
            public void onFailure(Call<ShoppingResults> call, Throwable t) {
            }
        });


    }

    public void initScroll() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.d("scroll", layoutManager.findLastVisibleItemPosition() + " | " + (adapter.getItemCount() - 1));
                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1) {
                        //리스트 마지막
                        SearchRetrofitMore(key);
                        isLoading = true;
                    }
                }
            }
        });

    }


}
