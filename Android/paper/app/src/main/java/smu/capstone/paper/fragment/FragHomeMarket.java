package smu.capstone.paper.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.StickerDetailActivity;
import smu.capstone.paper.activity.StickerSearchActivity;
import smu.capstone.paper.adapter.HomeMarketAdapter;
import smu.capstone.paper.item.HomeMarketItem;

public  class FragHomeMarket extends Fragment {
    private View view;
    private SearchView searchView;
    ArrayList<HomeMarketItem> items = new ArrayList<HomeMarketItem>();

    public void addItem(HomeMarketItem item){
        items.add(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_market, container, false);

        searchView = view.findViewById(R.id.market_search);
        GridView gridView = view.findViewById(R.id.market_grid);

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

        // 아이템 추가
        addItem(new HomeMarketItem(R.drawable.sampleimg, "sample1", "20p"));
        addItem(new HomeMarketItem(R.drawable.test, "sample2", "10p"));
        addItem(new HomeMarketItem(R.drawable.ic_favorite, "sample3", "50p"));
        addItem(new HomeMarketItem(R.drawable.test, "sample4", "60p"));
        addItem(new HomeMarketItem(R.drawable.test, "sample5", "80p"));
        addItem(new HomeMarketItem(R.drawable.test, "sample6", "100p"));
        addItem(new HomeMarketItem(R.drawable.test, "sample7", "200p"));
        addItem(new HomeMarketItem(R.drawable.test, "sample8", "30p"));
        addItem(new HomeMarketItem(R.drawable.test, "sample9", "25p"));

        HomeMarketAdapter adapter = new HomeMarketAdapter(this.getContext(), R.layout.market_item, items);
        gridView.setAdapter(adapter);

        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getContext(), StickerDetailActivity.class);
                intent.putExtra("image",items.get(position).getImg());
                intent.putExtra("name", items.get(position).getIname());
                intent.putExtra("price", items.get(position).getIcost());
                startActivity(intent);
                //Log.d("TAG", position + "is Clicked");      // Can not getting this method.

            }
        });

        return view;
    }
}