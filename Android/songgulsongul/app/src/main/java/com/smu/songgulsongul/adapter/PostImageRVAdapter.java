package com.smu.songgulsongul.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.PostActivity;
import com.smu.songgulsongul.responseData.Post;
import com.smu.songgulsongul.server.RetrofitClient;

public class PostImageRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    Context context;
    List<Post> items;

    public PostImageRVAdapter(Context context, List<Post> items){
        this.context = context;
        this.items = items;
    }

    public void addItem( List<Post> posts){
        items.addAll(posts);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_image_item, parent, false);
            return new PostImageRVAdapter.ItemViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            return new PostImageRVAdapter.LoadingViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PostImageRVAdapter.ItemViewHolder) {
            Glide.with(context).load(RetrofitClient.getBaseUrl()+ items.get(position).getImage())
                    .into(((ItemViewHolder) holder).img); // 게시물 사진 세팅

            ((ItemViewHolder) holder).img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostActivity.class);
                    // 게시글 id 전달
                    int postId = items.get(position).getId();
                    intent.putExtra("post_id", postId);
                    context.startActivity(intent);
                }
            });
        }
        else {//가독성을 위해 적어놓음?..
            showLoadingView((PostImageRVAdapter.LoadingViewHolder) holder, position);
        }
    }

    private void showLoadingView(PostImageRVAdapter.LoadingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }




    // Item Holders


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.post_image_iv);


        }

    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.item_progressBar);
        }
    }

}
