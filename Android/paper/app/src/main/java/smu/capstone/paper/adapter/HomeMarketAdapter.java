package smu.capstone.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smu.capstone.paper.R;
import smu.capstone.paper.item.HomeMarketItem;
import smu.capstone.paper.responseData.Sticker;
import smu.capstone.paper.server.RetrofitClient;

public class HomeMarketAdapter extends BaseAdapter {
    List<Sticker> items;
    private Context mContext;
    LayoutInflater inf;
    int layout;
    int itemCnt;

    public HomeMarketAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public HomeMarketAdapter(Context mContext, int layout, List<Sticker> items) {
        this.mContext = mContext;
        inf =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.items = items;
        itemCnt = items.size();
    }

    // 받아온 데이터로 마켓 아이템 내용 셋팅
    public void setItem(Sticker sticker, ImageView imageView, TextView nameText, TextView costText){
        Glide.with(mContext).load(RetrofitClient.getBaseUrl() + sticker.getImage()).into(imageView); // 사진
        nameText.setText(sticker.getName()); // 상품명
        costText.setText(sticker.getPrice() + "p"); // 가격
    }

    @Override
    public int getCount() {
        return itemCnt;
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
        Sticker sticker = (Sticker)items.get(position);

        if (convertView==null)
            convertView = inf.inflate(layout, null);

        ImageView imageView = convertView.findViewById(R.id.market_item_img);
        TextView nameText = convertView.findViewById(R.id.market_item_name);
        TextView costText = convertView.findViewById(R.id.market_item_cost);

        setItem(sticker, imageView, nameText, costText);

        return convertView;
    }
}
