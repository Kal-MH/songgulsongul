package smu.capstone.paper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeMarketAdapter extends BaseAdapter {
    ArrayList<HomeMarketItem> items = new ArrayList<HomeMarketItem>();
    private Context mContext;

    public HomeMarketAdapter(Context mContext) {this.mContext = mContext;}

    public void addItem(HomeMarketItem item){
        items.add(item);
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

        if (convertView==null) {
            LayoutInflater inf = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.market_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.market_item_img);
        TextView nameText = convertView.findViewById(R.id.market_item_name);
        TextView costText = convertView.findViewById(R.id.market_item_cost);
        imageView.setImageResource(homeMarketItem.getImg());
        nameText.setText(homeMarketItem.getIname());
        costText.setText(homeMarketItem.getIcost());

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(340, 350));

        return convertView;
    }
}
