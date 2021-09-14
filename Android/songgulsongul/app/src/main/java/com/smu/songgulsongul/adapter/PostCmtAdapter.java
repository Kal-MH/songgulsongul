package com.smu.songgulsongul.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.ProfileActivity;
import com.smu.songgulsongul.responseData.Comment;
import com.smu.songgulsongul.server.DefaultImage;
import com.smu.songgulsongul.server.RetrofitClient;

public class PostCmtAdapter extends RecyclerView.Adapter<PostCmtAdapter.ViewHolder> {

    final Context context;
    List<Comment> dataList;
    int cmtCnt;

    private OnItemLongClickEventListener mItemLongClickListener;

    Date today = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    Calendar rightNow = Calendar.getInstance();

    public interface OnItemLongClickEventListener {
        void onItemLongClick(View a_view, int a_position);
    }

    public void setOnItemLongClickListener(OnItemLongClickEventListener a_listener) {
        mItemLongClickListener = a_listener;
    }

    public PostCmtAdapter(Context context, List<Comment> obj) {
        this.context = context;
        dataList = obj;
        cmtCnt = dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.post_cmt_item, parent, false);
        return new PostCmtAdapter.ViewHolder(itemView, mItemLongClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull PostCmtAdapter.ViewHolder holder, final int position) {

        final Comment item = dataList.get(position);
        holder.userId.setText(item.getLogin_id());
        holder.cmt.setText(item.getText());

        if (date.format(today).equals((item.getC_date()))) {
            int hour = Integer.parseInt(item.getC_time().substring(0, 2));
            if (hour != rightNow.get(Calendar.HOUR_OF_DAY)) {
                holder.date.setText((rightNow.get(Calendar.HOUR_OF_DAY) - hour) + "시간 전");
            } else {
                int min = Integer.parseInt(item.getC_time().substring(3, 5));
                if (min == rightNow.get(Calendar.MINUTE))
                    holder.date.setText("방금 게시됨");
                else
                    holder.date.setText((rightNow.get(Calendar.MINUTE) - min) + "분 전");
            }
        } else
            holder.date.setText(item.getC_date()); //게시 날짜

        String pro_img = item.getImg_profile();
        String img_addr;
        if (pro_img.equals(DefaultImage.DEFAULT_IMAGE))
            img_addr = RetrofitClient.getBaseUrl() + pro_img;
        else
            img_addr = pro_img;
        Glide.with(context).load(img_addr).into(holder.profile);


        holder.userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                // 게시글 사용자 id 전달
                intent.putExtra("userId", item.getLogin_id());
                context.startActivity(intent);
            }
        });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                // 게시글 사용자 id 전달
                intent.putExtra("userId", item.getLogin_id());
                context.startActivity(intent);
            }
        });


    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userId;
        TextView cmt;
        ImageView profile;
        TextView date;

        public ViewHolder(@NonNull final View itemView, final OnItemLongClickEventListener a_itemLongClickListener) {
            super(itemView);
            userId = (TextView) itemView.findViewById(R.id.post_cmt_item_id);
            cmt = (TextView) itemView.findViewById(R.id.post_item_cmt);
            profile = itemView.findViewById(R.id.post_item_cmt_pic);
            date = itemView.findViewById(R.id.post_cmt_item_date);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        a_itemLongClickListener.onItemLongClick(itemView, position);
                    }
                    return false;
                }
            });
        }
    }

}
