package smu.capstone.paper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.AddItemtagActivity;
import smu.capstone.paper.server.RetrofitClient;


public class AddItemTagAdapter extends ItemTagAdapter {
    public AddItemTagAdapter(Context context, JsonArray obj)  {
        super(context, obj);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        JsonObject item = dataList.get(position).getAsJsonObject();
        if(item.get("id").getAsInt() == -1 ){
            Resources res = context.getResources();
            Drawable myImage = ResourcesCompat.getDrawable(res, R.drawable.ic_baseline_add_24, null);
            holder.pic.setImageDrawable(myImage);
        }
        else
            Glide.with(context).load(RetrofitClient.getBaseUrl() + item.get("picture").getAsString() ).into(holder.pic); // 게시물 사진

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( dataList.get(position).getAsJsonObject().get("id").getAsInt() == -1){
                    Log.d("TAG","add action");
                    Intent intent = new Intent(context, AddItemtagActivity.class);
                    context.startActivity(intent);
                }
            }
        });


    }

}
