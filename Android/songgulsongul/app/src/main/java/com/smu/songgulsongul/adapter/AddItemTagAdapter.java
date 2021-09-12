package com.smu.songgulsongul.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.zip.Inflater;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.AddItemtagActivity;
import com.smu.songgulsongul.activity.EditAccountActivity;
import com.smu.songgulsongul.activity.ItemDetailActivity;
import com.smu.songgulsongul.responseData.ItemTag;


public class AddItemTagAdapter extends ItemTagAdapter {
    public AddItemTagAdapter(Context context, List<ItemTag> obj)  {
        super(context, obj);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final ItemTag item = dataList.get(position);
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
                    mListener.onItemClick(v);
                }
                else{
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("hprice", item.getH_price().equals("-1") ? "" : item.getH_price());
                    intent.putExtra("lprice", item.getL_price().equals("-1") ? "" : item.getL_price());
                    intent.putExtra("url", item.getUrl());
                    intent.putExtra("picture", item.getPicture());

                    context.startActivity(intent);
                }
            }
        });

        holder.pic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*new AlertDialog.Builder(context)
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //삭제하는 코드
                                dataList.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();*/
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.activity_popup,null);
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView icon=dialogView.findViewById(R.id.warning);
                icon.setVisibility(View.GONE);

                TextView txt=dialogView.findViewById(R.id.txtText);
                txt.setText("삭제하시겠습니까?");

                Button ok_btn = dialogView.findViewById(R.id.okBtn);
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //삭제하는 코드
                        dataList.remove(position);
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });

                Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                return false;
            }
        });
    }


    //item 클릭 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v);
    }

    private AddItemTagAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(AddItemTagAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }
}
