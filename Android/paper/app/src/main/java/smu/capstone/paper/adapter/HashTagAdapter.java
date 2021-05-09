package smu.capstone.paper.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.ItemDetailActivity;
import smu.capstone.paper.item.HashtagItem;
import smu.capstone.paper.item.ItemtagItem;

public class HashTagAdapter extends RecyclerView.Adapter<HashTagAdapter.ViewHolder>{
    Context context;
    JsonArray dataList;
    LayoutInflater inf;
    int hashCnt;
    int layout;


    public HashTagAdapter(Context context, JsonArray obj){
        this.context = context;
        dataList = obj;
        hashCnt = dataList.size();
    }

    @NonNull
    @Override
    public HashTagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.post_hashtag_item, parent, false);
        return new HashTagAdapter.ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull HashTagAdapter.ViewHolder holder, final int position) {
        JsonObject item = dataList.get(position).getAsJsonObject();
        holder.content.setText("#"+item.get("text").getAsString() +" ");
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            content = (TextView)itemView.findViewById(R.id.hash_tag_content);
        }
    }
}
