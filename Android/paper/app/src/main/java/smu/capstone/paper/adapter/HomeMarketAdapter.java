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

import smu.capstone.paper.R;
import smu.capstone.paper.item.HomeMarketItem;

public class HomeMarketAdapter extends BaseAdapter {
    //ArrayList<HomeMarketItem> items;
    JSONObject obj = new JSONObject();
    JSONArray dataList;
    private Context mContext;
    LayoutInflater inf;
    int layout;
    int itemCnt;

    public HomeMarketAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public HomeMarketAdapter(Context mContext, int layout, JSONObject obj) throws JSONException {
        this.mContext = mContext;
        inf =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.obj = obj;
        dataList = obj.getJSONArray("data");
        itemCnt = dataList.length();
    }

    // 받아온 데이터로 마켓 아이템 내용 셋팅
    public void setItem(JSONObject item, ImageView imageView, TextView nameText, TextView costText){
        try {
            Glide.with(mContext).load(item.getInt("marketImage")).into(imageView); // 사진
            nameText.setText(item.getString("name")); // 상품명
            costText.setText(item.getString("price")); // 가격
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return itemCnt;
    }

    @Override
    public Object getItem(int position) {
        JSONObject item = new JSONObject();
        try {
            item = dataList.getJSONObject(position);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //HomeMarketItem homeMarketItem = items.get(position);
        JSONObject item = new JSONObject();
        try{
            item = dataList.getJSONObject(position);
        }catch (JSONException e){
            e.printStackTrace();
        }

        if (convertView==null)
            convertView = inf.inflate(layout, null);

        ImageView imageView = convertView.findViewById(R.id.market_item_img);
        TextView nameText = convertView.findViewById(R.id.market_item_name);
        TextView costText = convertView.findViewById(R.id.market_item_cost);

        setItem(item, imageView, nameText, costText);

        return convertView;
    }
}
