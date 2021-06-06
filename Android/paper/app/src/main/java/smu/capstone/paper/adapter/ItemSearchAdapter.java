package smu.capstone.paper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.item.ItemSearchItem;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchAdapter.ViewHolder> {
    private ArrayList<ItemSearchItem> mlist;

    public ItemSearchAdapter(ArrayList<ItemSearchItem> list) {
        mlist = list;
    }

    @NonNull
    @Override
    public ItemSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.itemsearch_item, parent, false);
        ItemSearchAdapter.ViewHolder vh = new ItemSearchAdapter.ViewHolder(itemView);

        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ItemSearchAdapter.ViewHolder holder, int position) {
        ItemSearchItem item = mlist.get(position);

        Glide.with(holder.picurl.getContext()).load(item.getPic()).override(100,100).into(holder.picurl); // 제품 사진
        //Glide.with(holder.picurl.getContext()).load("https://shopping-phinf.pstatic.net/main_8177959/81779594902.2.jpg").override(100,100).into(holder.picurl);
        holder.name.setText(item.getName()); //제품 이름
        holder.lprice.setText(item.getLprice()); //제품 최저가
        holder.hprice.setText(item.getHprice()); //제품 최고가
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView picurl;
        TextView name;
        TextView lprice;
        TextView hprice;

        ViewHolder(@NonNull View itemView){
            super(itemView);

            picurl = itemView.findViewById(R.id.itemseach_item_img);
            name = itemView.findViewById(R.id.itemseach_item_name);
            lprice = itemView.findViewById(R.id.itemseach_item_lprice);
            hprice = itemView.findViewById(R.id.itemseach_item_hprice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }
    }

    //item 클릭 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick (View v, int pos);
    }

    private ItemSearchAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(ItemSearchAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }
}
