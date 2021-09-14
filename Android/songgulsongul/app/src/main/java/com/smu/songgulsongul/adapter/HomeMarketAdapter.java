package com.smu.songgulsongul.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.StickerDetailActivity;
import com.smu.songgulsongul.responseData.Sticker;

public class HomeMarketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    List<Sticker> items;
    private final Context context;

    public void addItem(List<Sticker> stickers) {
        items.addAll(stickers);
    }


    public HomeMarketAdapter(Context context, List<Sticker> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_item, parent, false);
            return new HomeMarketAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            return new HomeMarketAdapter.LoadingViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HomeMarketAdapter.ItemViewHolder) {

            Glide.with(context).load(items.get(position).getImage())
                    .into(((HomeMarketAdapter.ItemViewHolder) holder).img);
            ((ItemViewHolder) holder).name.setText(items.get(position).getName());
            ((ItemViewHolder) holder).cost.setText(items.get(position).getPrice() + "p");


        } else {//가독성을 위해 적어놓음?..
            showLoadingView((HomeMarketAdapter.LoadingViewHolder) holder, position);
        }
    }

    private void showLoadingView(HomeMarketAdapter.LoadingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    // Item Holders


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, cost;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.market_item_img);
            name = itemView.findViewById(R.id.market_item_name);
            cost = itemView.findViewById(R.id.market_item_cost);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, StickerDetailActivity.class);
                        intent.putExtra("sticker_id", items.get(position).getId());
                        context.startActivity(intent);
                    }
                }
            });
        }

    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.item_progressBar);
        }
    }


}
