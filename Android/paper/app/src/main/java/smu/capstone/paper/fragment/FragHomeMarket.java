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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.StickerDetailActivity;
import smu.capstone.paper.activity.StickerSearchActivity;
import smu.capstone.paper.adapter.HomeMarketAdapter;
import smu.capstone.paper.item.HomeFeedItem;
import smu.capstone.paper.item.HomeMarketItem;

public  class FragHomeMarket extends Fragment {
    private View view;
    private SearchView searchView;
    HomeMarketAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_market, container, false);

        searchView = view.findViewById(R.id.market_search);
        GridView gridView = view.findViewById(R.id.market_grid);

        final JSONObject obj = getMarketData();

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
                intent.putExtra("search", query); // 검색한 내용 전달
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { // 검색어 입력 시 발생
                return false;
            }
        });

        try {
           adapter = new HomeMarketAdapter(this.getContext(), R.layout.market_item, obj);
        } catch (JSONException e){
            e.printStackTrace();
        }
        gridView.setAdapter(adapter);

        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getContext(), StickerDetailActivity.class);
                // 임시로 내용 전달 --> 실제로는 stickerId만 전달
                try {
                    intent.putExtra("stickerId",  obj.getJSONArray("data").getJSONObject(position).getInt("id"));
                    intent.putExtra("image", obj.getJSONArray("data").getJSONObject(position).getInt("image"));
                    intent.putExtra("name", obj.getJSONArray("data").getJSONObject(position).getString("name"));
                    intent.putExtra("price", obj.getJSONArray("data").getJSONObject(position).getInt("price"));
                    intent.putExtra("comment", obj.getJSONArray("data").getJSONObject(position).getString("text"));
                    startActivity(intent);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                Log.d("TAG", position + "is Clicked");      // Can not getting this method.

            }
        });

        return view;
    }

    //server에서 data전달
    public JSONObject getMarketData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();

        //임시 데이터 저장
        try{
            JSONObject obj = new JSONObject();
            obj.put("image", R.drawable.sampleimg);
            obj.put("name", "sample1");
            obj.put("price", "20");
            obj.put("id", 1);
            obj.put("text", "스티커 샘플 1 입니다~");
            arr.put(obj);

            JSONObject obj2 = new JSONObject();
            obj2.put("image", R.drawable.test);
            obj2.put("name", "sample2");
            obj2.put("price", "10");
            obj2.put("id", 2);
            obj2.put("text", "스티커 샘플 2 입니다~");
            arr.put(obj2);

            JSONObject obj3 = new JSONObject();
            obj3.put("image", R.drawable.ic_favorite);
            obj3.put("name", "sample3");
            obj3.put("price", "50");
            obj3.put("id", 3);
            obj3.put("text", "스티커 샘플 3 입니다~");
            arr.put(obj3);

            JSONObject obj4 = new JSONObject();
            obj4.put("image", R.drawable.ic_favorite_border);
            obj4.put("name", "sample4");
            obj4.put("price", "40");
            obj4.put("id", 4);
            obj4.put("text", "스티커 샘플 4 입니다~");
            arr.put(obj4);

            JSONObject obj5 = new JSONObject();
            obj5.put("image", R.drawable.sampleimg);
            obj5.put("name", "sample5");
            obj5.put("price", "20");
            obj5.put("id", 5);
            obj5.put("text", "스티커 샘플 5 입니다~");
            arr.put(obj5);

            JSONObject obj6 = new JSONObject();
            obj6.put("image", R.drawable.test);
            obj6.put("name", "sample6");
            obj6.put("price", "10");
            obj6.put("id", 6);
            obj6.put("text", "스티커 샘플 6 입니다~");
            arr.put(obj6);

            JSONObject obj7 = new JSONObject();
            obj7.put("image", R.drawable.ic_favorite);
            obj7.put("name", "sample7");
            obj7.put("price", "50");
            obj7.put("id", 7);
            obj7.put("text", "스티커 샘플 7 입니다~");
            arr.put(obj7);

            JSONObject obj8 = new JSONObject();
            obj8.put("image", R.drawable.ic_favorite_border);
            obj8.put("name", "sample8");
            obj8.put("price", "40");
            obj8.put("id", 8);
            obj8.put("text", "스티커 샘플 8 입니다~");
            arr.put(obj8);

            item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }

}