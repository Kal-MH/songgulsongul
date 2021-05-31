package smu.capstone.paper.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.R;
import smu.capstone.paper.activity.ProfileActivity;
import smu.capstone.paper.activity.StickerDetailActivity;
import smu.capstone.paper.activity.StickerSearchActivity;
import smu.capstone.paper.adapter.HomeMarketAdapter;
import smu.capstone.paper.responseData.MarketResponse;
import smu.capstone.paper.responseData.Sticker;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public  class FragHomeMarket extends Fragment {
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    private View view;
    private SearchView searchView;
    HomeMarketAdapter adapter;
    GridView gridView;

    List<Sticker> stickers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_market, container, false);

        searchView = view.findViewById(R.id.market_search);
        gridView = view.findViewById(R.id.market_grid);

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

        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getContext(), StickerDetailActivity.class);

                // sticker_id 전달
                intent.putExtra("sticker_id", stickers.get(position).getId());
                Log.d("TAG", position + "is Clicked");      // Can not getting this method.

            }
        });

        return view;
    }

    public void setMarketData(){
        adapter = new HomeMarketAdapter(this.getContext(), R.layout.market_item, stickers);
        gridView.setAdapter(adapter);
    }

    //server에서 data전달
    public void getMarketData(){
       serviceApi.MarketMain(null).enqueue(new Callback<MarketResponse>() {
           @Override
           public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
               MarketResponse result = response.body();
               int resultCode = result.getCode();
               if(resultCode == StatusCode.RESULT_OK){
                   stickers = result.getMarketItem();
                   setMarketData();
               }
               else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                   Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<MarketResponse> call, Throwable t) {
               Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
               Log.e("마켓 불러오기 에러", t.getMessage());
               t.printStackTrace(); // 에러 발생 원인 단계별로 출력
           }
       });
    }

}