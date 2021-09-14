package com.smu.songgulsongul.recycler_adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.recycler_item.HashTag;

public class HashTagAdapter extends RecyclerView.Adapter<HashTagAdapter.ViewHolder> {
    Context context;
    List<HashTag> dataList;
    LayoutInflater inf;
    int hashCnt;
    int layout;


    public HashTagAdapter(Context context, List<HashTag> obj) {
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
        HashTag item = dataList.get(position);
        holder.content.setText("#" + item.getText() + " ");
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


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.hash_tag_content);
        }
    }
}
