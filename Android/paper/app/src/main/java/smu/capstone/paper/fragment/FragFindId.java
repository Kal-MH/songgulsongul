package smu.capstone.paper.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.HomeMarketAdapter;
import smu.capstone.paper.item.HomeMarketItem;

public class FragFindId extends Fragment {

    public FragFindId() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_find_id, container, false);

    }

    public static class FragHomeMarket extends Fragment {
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
                    Log.d("TAG", position + "is Clicked");      // Can not getting this method.

                }
            });

            return view;
        }
    }

}
