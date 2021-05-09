package smu.capstone.paper.adapter;

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
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.item.HomeFeedItem;
import smu.capstone.paper.item.ItemtagItem;
import smu.capstone.paper.item.PostItem;
import smu.capstone.paper.server.RetrofitClient;

public class ItemTagAdapter extends RecyclerView.Adapter<ItemTagAdapter.ViewHolder> {
    Context context;
    JsonArray dataList;
    int itemCnt;

    public ItemTagAdapter(Context context, JsonArray obj) {
        this.context = context;

        dataList =obj;
        itemCnt = dataList.size();
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

        JsonObject item = dataList.get(position).getAsJsonObject();
        Glide.with(context).load(RetrofitClient.getBaseUrl() + item.get("picture").getAsString() ).into(holder.pic); // 게시물 사진

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            pic = (ImageView)itemView.findViewById(R.id.item_tag_img);
        }
    }
}
