package smu.capstone.paper.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.StickerDetailActivity;
import smu.capstone.paper.item.StickerItem;

public class StickerSearchAdapter extends RecyclerView.Adapter<StickerSearchAdapter.ViewHolder> {
    private Context context;
    JSONObject obj = new JSONObject();
    JSONArray dataList;
    int itemCnt;

    public StickerSearchAdapter(Context context, JSONObject obj) throws JSONException {
        this.context = context;
        this.obj = obj;
        dataList = obj.getJSONArray("data");
        itemCnt = dataList.length();
    }

    // 받아온 데이터로 스티커 내용 셋팅
    public void setItem(@NonNull StickerSearchAdapter.ViewHolder holder, JSONObject item){
        try {
            holder.sticker_price.setText(item.getString("stickerPrice"));
            holder.sticker_name.setText(item.getString("stickerName"));
            Glide.with(context).load(item.getInt("stickerImage")).into(holder.sticker_image);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public StickerSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.sticker_search_item, parent, false);
        return new StickerSearchAdapter.ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull StickerSearchAdapter.ViewHolder holder, final int position) {
        try{
            JSONObject item = dataList.getJSONObject(position);
            setItem(holder, item);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemCnt;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sticker_image;
        TextView sticker_name;
        TextView sticker_price;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            sticker_image = (ImageView)itemView.findViewById(R.id.stk_sitem_image);
            sticker_name = (TextView)itemView.findViewById(R.id.stk_sitem_name);
            sticker_price = (TextView)itemView.findViewById(R.id.stk_sitem_price);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    try {
                        JSONObject item = dataList.getJSONObject(pos);
                        if (pos != RecyclerView.NO_POSITION) {
                            // 임시로 내용 전달 --> 실제로는 stickerId만 전달
                            Intent intent = new Intent(context, StickerDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("stickerId", item.getInt("stickerId"));
                            intent.putExtra("image", item.getInt("stickerImage"));
                            intent.putExtra("name", item.getString("stickerName"));
                            intent.putExtra("price", item.getString("stickerPrice"));

                            context.startActivity(intent);
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
