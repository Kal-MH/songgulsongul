package smu.capstone.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.item.HomeMarketItem;

public class HomeMarketAdapter extends BaseAdapter {
    ArrayList<HomeMarketItem> items;
    private Context mContext;
    LayoutInflater inf;
    int layout;

    public HomeMarketAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public HomeMarketAdapter(Context mContext, int layout, ArrayList<HomeMarketItem> items) {
        this.mContext = mContext;
        inf =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.items = items;
    }

    public void setItem(HomeMarketItem item, ImageView imageView, TextView nameText, TextView costText){
        // 받아온 데이터로 마켓 아이템 내용 셋팅
        Glide.with(mContext).load(item.getImg()).into(imageView); // 사진
        nameText.setText(item.getIname()); // 상품명
        costText.setText(item.getIcost()); // 가격
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeMarketItem homeMarketItem = items.get(position);

        if (convertView==null)
            convertView = inf.inflate(layout, null);

        ImageView imageView = convertView.findViewById(R.id.market_item_img);
        TextView nameText = convertView.findViewById(R.id.market_item_name);
        TextView costText = convertView.findViewById(R.id.market_item_cost);

        setItem(homeMarketItem, imageView, nameText, costText);

        return convertView;
    }
}
