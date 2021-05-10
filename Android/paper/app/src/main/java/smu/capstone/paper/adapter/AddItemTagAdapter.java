package smu.capstone.paper.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import smu.capstone.paper.activity.AddItemtagActivity;
import smu.capstone.paper.server.RetrofitClient;


public class AddItemTagAdapter extends ItemTagAdapter {
    public AddItemTagAdapter(Context context, JsonArray obj)  {
        super(context, obj);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        JsonObject item = dataList.get(position).getAsJsonObject();
        Glide.with(context).load(RetrofitClient.getBaseUrl() + item.get("picture").getAsString() ).into(holder.pic); // 게시물 사진

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    Log.d("TAG","add action");
                    Intent intent = new Intent(context, AddItemtagActivity.class);
                    context.startActivity(intent);
                }
            }
        });


    }

}
