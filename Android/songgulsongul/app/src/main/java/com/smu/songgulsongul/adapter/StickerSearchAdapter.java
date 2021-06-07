package com.smu.songgulsongul.adapter;

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

import java.util.List;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.StickerDetailActivity;
import com.smu.songgulsongul.responseData.Sticker;
import com.smu.songgulsongul.server.RetrofitClient;

public class StickerSearchAdapter extends RecyclerView.Adapter<StickerSearchAdapter.ViewHolder> {
    private Context context;
    List<Sticker> items;
    int itemCnt;

    public StickerSearchAdapter(Context context, List<Sticker> items) {
        this.context = context;
        this.items = items;
        itemCnt = items.size();
    }

    // 받아온 데이터로 스티커 내용 셋팅
    public void setItem(@NonNull StickerSearchAdapter.ViewHolder holder, Sticker sticker){
        holder.sticker_price.setText(sticker.getPrice() + "p");
        holder.sticker_name.setText(sticker.getName());
        Glide.with(context).load(RetrofitClient.getBaseUrl() + sticker.getImage()).into(holder.sticker_image);
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
        Sticker sticker = items.get(position);
        setItem(holder, sticker);
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
                    Sticker sticker = items.get(pos);
                    if (pos != RecyclerView.NO_POSITION) {
                        // stickerId 전달
                        Intent intent = new Intent(context, StickerDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("sticker_id", sticker.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
