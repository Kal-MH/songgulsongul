package smu.capstone.paper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.R;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchAdapter.ViewHolder>{
    private Context context;
    JSONObject obj = new JSONObject();
    JSONArray dataList;
    int itemCnt;

    public ItemSearchAdapter(Context context, JSONObject obj) throws JSONException {
        this.context = context;
        this.obj = obj;

        dataList = obj.getJSONArray("data");
        itemCnt = dataList.length();
    }

    public void setItem(@NonNull ViewHolder holder, JSONObject item){
        // 받아온 데이터로 셋팅
        try {
            Glide.with(context).load(item.getInt("tag_img")).into(holder.pic); // 제품 사진
            holder.name.setText(item.getString("tag_name")); //제품 이름
            holder.lprice.setText(item.getString("low_price")); //제품 최저가
            holder.hprice.setText(item.getString("high_price")); //제품 최고가
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.itemsearch_item, parent, false);
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject item = dataList.getJSONObject(position);
            Glide.with(context).load(item.get("tag_img")).into(holder.pic); // 제품 사진
            holder.name.setText(item.getString("tag_name")); //제품 이름
            holder.lprice.setText(item.getString("low_price")); //제품 최저가
            holder.hprice.setText(item.getString("high_price")); //제품 최고가
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView name;
        TextView lprice;
        TextView hprice;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            pic = (ImageView)itemView.findViewById(R.id.itemseach_item_img);
            name = (TextView)itemView.findViewById(R.id.itemseach_item_name);
            lprice = (TextView)itemView.findViewById(R.id.itemseach_item_lprice);
            hprice = (TextView)itemView.findViewById(R.id.itemseach_item_hprice);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
