package smu.capstone.paper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.ItemDetailActivity;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.item.HomeFeedItem;
import smu.capstone.paper.item.ItemtagItem;
import smu.capstone.paper.item.PostItem;

public class ItemTagAdapter extends RecyclerView.Adapter<ItemTagAdapter.ViewHolder> {
    Context context;
    JSONObject obj = new JSONObject();
    JSONArray dataList;
    LayoutInflater inf;
    int itemCnt;
    int layout;

    public ItemTagAdapter(Context context,JSONObject obj) throws JSONException{
        this.context = context;
        //this.items = items;
        this.obj = obj;

        dataList = obj.getJSONArray("data");
        itemCnt = dataList.length();
    }


    public void setItem(@NonNull ItemTagAdapter.ViewHolder holder, JSONObject item, int position){
        // 받아온 데이터로 셋팅
        try {
            Glide.with(context).load(item.getInt("picture")).into(holder.pic);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.itemtag_item, parent, false);
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            final JSONObject item = dataList.getJSONObject(position);
            holder.pic.setImageBitmap((Bitmap) item.get("picture"));
        } catch (JSONException e){
            e.printStackTrace();

        }

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, ItemDetailActivity.class);

                ((Activity)context).startActivityForResult(intent,1234);*/
                if(mListener != null){
                    mListener.onItemClick(v);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.length();
    }
/*
    public void insertItem(ItemtagItem data){
        items.add(data);
    }*/

    static class ViewHolder extends RecyclerView.ViewHolder{
        int pos = getAdapterPosition();
        ImageView pic;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            pic = (ImageView)itemView.findViewById(R.id.item_tag_img);

        }

    }
    //item 클릭 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v);
    }

    private ItemTagAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(ItemTagAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }
}
