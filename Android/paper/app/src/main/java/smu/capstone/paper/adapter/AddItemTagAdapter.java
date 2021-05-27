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

import java.util.List;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.AddItemtagActivity;
import smu.capstone.paper.responseData.ItemTag;
import smu.capstone.paper.server.RetrofitClient;


public class AddItemTagAdapter extends ItemTagAdapter {
    public AddItemTagAdapter(Context context, List<ItemTag> obj)  {
        super(context, obj);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ItemTag item = dataList.get(position);
        if(item.getId() == -1 ){
            Resources res = context.getResources();
            Drawable myImage = ResourcesCompat.getDrawable(res, R.drawable.ic_baseline_add_24, null);
            holder.pic.setImageDrawable(myImage);
        }
        else
            //Glide.with(context).load(RetrofitClient.getBaseUrl() + item.getPicture() ).into(holder.pic); // 게시물 사진
            // item tag 추가의 경우 naver api 이용한 http 이미지 링크 받아오므로 baseUrl 제거 --> 실제 이 코드 사용
            Glide.with(context).load(item.getPicture() ).into(holder.pic); // 게시물 사진

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( dataList.get(position).getId()== -1){
                    Log.d("TAG","add action");
                    Intent intent = new Intent(context, AddItemtagActivity.class);
                    context.startActivity(intent);
                }
            }
        });

    }

}
