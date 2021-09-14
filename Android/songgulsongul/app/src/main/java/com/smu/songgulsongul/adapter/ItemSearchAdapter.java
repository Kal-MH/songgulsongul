package com.smu.songgulsongul.adapter;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.item.ItemSearchItem;

public class ItemSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<ItemSearchItem> data;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    Context context;

    public ItemSearchAdapter(Context context, ArrayList<ItemSearchItem> list) {
        this.context = context;
        data = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemsearch_item, parent, false);
            return new ItemSearchAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            return new ItemSearchAdapter.LoadingViewHolder(view);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemSearchAdapter.ItemViewHolder) {

            ItemSearchItem item = data.get(position);

            Glide.with(context).load(item.getImg()).into(((ItemViewHolder) holder).picurl); // 제품 사진
            String name = item.getTitle();
            name = name.replaceAll("<b>", "");
            name = name.replaceAll("<b>", "");
            name = name.replaceAll("</b>", "");
            name = name.replaceAll("&lt;", "<");
            name = name.replaceAll("&gt;", ">");
            name = name.replaceAll("&amp;", "&");
            ((ItemViewHolder) holder).name.setText(name); //제품 이름

            if (name.length() > 15)
                name = name.substring(0, 15);

            ((ItemViewHolder) holder).lprice.setText(item.getLprice()); //제품 최저가
            ((ItemViewHolder) holder).hprice.setText(item.getHprice()); //제품 최고가
        } else if (holder instanceof ItemSearchAdapter.LoadingViewHolder) {
            showLoadingView((ItemSearchAdapter.LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void showLoadingView(ItemSearchAdapter.LoadingViewHolder holder, int position) {

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView picurl;
        TextView name;
        TextView lprice;
        TextView hprice;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            picurl = itemView.findViewById(R.id.itemseach_item_img);
            name = itemView.findViewById(R.id.itemseach_item_name);
            lprice = itemView.findViewById(R.id.itemseach_item_lprice);
            hprice = itemView.findViewById(R.id.itemseach_item_hprice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
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

    //item 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private ItemSearchAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(ItemSearchAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }
}
