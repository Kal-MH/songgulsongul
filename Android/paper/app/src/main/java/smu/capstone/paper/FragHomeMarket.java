package smu.capstone.paper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragHomeMarket extends Fragment {
    private View view;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_market, container, false);

        searchView = view.findViewById(R.id.market_search);
        GridView gridView = view.findViewById(R.id.market_grid);

        HomeMarketAdapter adapter = new HomeMarketAdapter(this.getContext());
        adapter.addItem(new HomeMarketItem(R.drawable.sampleimg, "sample1", "20p"));
        adapter.addItem(new HomeMarketItem(R.drawable.sampleimg, "sample2", "10p"));
        adapter.addItem(new HomeMarketItem(R.drawable.sampleimg, "sample3", "50p"));
        adapter.addItem(new HomeMarketItem(R.drawable.sampleimg, "sample4", "60p"));
        adapter.addItem(new HomeMarketItem(R.drawable.sampleimg, "sample5", "80p"));
        adapter.addItem(new HomeMarketItem(R.drawable.sampleimg, "sample6", "100p"));
        adapter.addItem(new HomeMarketItem(R.drawable.sampleimg, "sample7", "200p"));
        adapter.addItem(new HomeMarketItem(R.drawable.sampleimg, "sample8", "30p"));
        adapter.addItem(new HomeMarketItem(R.drawable.sampleimg, "sample9", "25p"));
        gridView.setAdapter(adapter);

        return view;
    }
}
