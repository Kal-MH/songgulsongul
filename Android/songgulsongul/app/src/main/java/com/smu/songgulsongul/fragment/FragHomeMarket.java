package com.smu.songgulsongul.fragment;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.StickerSearchActivity;
import com.smu.songgulsongul.adapter.HomeMarketAdapter;
import com.smu.songgulsongul.responseData.MarketResponse;
import com.smu.songgulsongul.responseData.Sticker;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public  class FragHomeMarket extends Fragment {
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    private View view;
    private SearchView searchView;
    HomeMarketAdapter adapter;
    RecyclerView recyclerView;

    List<Sticker> stickerData;

    int lastId;
    boolean isLoading = false;
    LinearLayout loadlayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_market, container, false);

        searchView = view.findViewById(R.id.market_search);
        recyclerView = view.findViewById(R.id.market_grid);
        loadlayout = view.findViewById(R.id.market_load_layout);
        loadlayout.setVisibility(View.GONE);
        loadlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //리스트 마지막
                getMarketDataMore();
                isLoading = true;
                loadlayout.setVisibility(View.GONE);
            }
        });




        initScroll();
        getMarketData();



        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // 검색 버튼 눌렀을 시 발생
                Intent intent = new Intent(getActivity(), StickerSearchActivity.class);
                intent.putExtra("keyword", query); // 검색한 내용 전달
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { // 검색어 입력 시 발생
                return false;
            }
        });


        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) searchView.findViewById(id);
        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.ibm_plex_sans_light);
        searchText.setTypeface(tf);

        getMarketData();


        return view;
    }

    //server에서 data전달
    public void getMarketData(){
       serviceApi.MarketMain(null).enqueue(new Callback<MarketResponse>() {
           @Override
           public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
               MarketResponse result = response.body();
               int resultCode = result.getCode();
               if(resultCode == StatusCode.RESULT_OK){
                   stickerData = result.getMarketItem();
                   adapter = new HomeMarketAdapter(getContext(), stickerData);
                   GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                   recyclerView.setLayoutManager(layoutManager);
                   recyclerView.setAdapter(adapter);

                   initScroll();

               }
               else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                   Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
               }
           }

           @Override
           public void onFailure(Call<MarketResponse> call, Throwable t) {
               Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
               Log.e("마켓 불러오기 에러", t.getMessage());
               t.printStackTrace(); // 에러 발생 원인 단계별로 출력
           }
       });
    }


    public void getMarketDataMore(){

        lastId = stickerData.get(stickerData.size()-1).getId();
        stickerData.add(null);
        adapter.notifyItemInserted(stickerData.size() - 1);

        loadlayout.setVisibility(View.GONE);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                serviceApi.MarketMain(lastId).enqueue(new Callback<MarketResponse>() {
                    @Override
                    public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
                        stickerData.remove(stickerData.size()-1);
                        adapter.notifyItemRemoved(stickerData.size());
                        MarketResponse result = response.body();
                        int resultCode = result.getCode();
                        if(resultCode == StatusCode.RESULT_OK){
                            adapter.addItem( result.getMarketItem());
                            adapter.notifyDataSetChanged();

                        }
                        else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                            Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                        }

                        isLoading = false;

                    }

                    @Override
                    public void onFailure(Call<MarketResponse> call, Throwable t) {
                        Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                        Log.e("마켓 불러오기 에러", t.getMessage());
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    public void initScroll(){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.d("scroll" , layoutManager.findLastCompletelyVisibleItemPosition() +" | " + ( adapter.getItemCount()  -1 ) );
                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition()== adapter.getItemCount() - 1) {
                        loadlayout.setVisibility(View.VISIBLE);
                    }
                    else
                        loadlayout.setVisibility(View.GONE);
                }
                else{
                    loadlayout.setVisibility(View.GONE);
                }
            }
        });

    }
}