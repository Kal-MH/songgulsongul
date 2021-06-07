package com.smu.songgulsongul.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.PostActivity;
import com.smu.songgulsongul.activity.ProfileActivity;
import com.smu.songgulsongul.responseData.CodeResponse;
import com.smu.songgulsongul.responseData.Post;
import com.smu.songgulsongul.responseData.PostFeed;
import com.smu.songgulsongul.responseData.User;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class HomeFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    Context context;

    Date today = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    Calendar rightNow = Calendar.getInstance();

    List<PostFeed> items;
    int itemCnt;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;

    public HomeFeedAdapter (Context context, List<PostFeed> items)  {
        this.context = context;
        this.items = items;
        itemCnt = items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
            return new ItemViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ItemViewHolder) {

            //post 가져옴
            final PostFeed item = items.get(position);
            final int postId = item.getPost().getId();

            setItem((ItemViewHolder) holder, position);
            // 좋아요 listener
            ((ItemViewHolder) holder).favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serviceApi.Like(LoginSharedPreference.getUserId(context),postId)
                            .enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                    int like = item.getLikeOnset();
                                    int resultCode = response.body().getCode();
                                    if( resultCode == statusCode.RESULT_OK){

                                        int likeNum = item.getLikeNum();
                                        if( like == 1){ //좋아요 취소하기
                                            like = 0;
                                            item.setLikeNum(likeNum-1);
                                        }
                                        else{
                                            like = 1;
                                            item.setLikeNum(likeNum+1);
                                        }
                                        item.setLikeOnset(like);
                                        notifyItemChanged(position);
                                    }
                                    else if( resultCode == statusCode.RESULT_CLIENT_ERR){
                                        Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else if( resultCode == statusCode.RESULT_SERVER_ERR){
                                        Toast.makeText(context, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CodeResponse> call, Throwable t) {
                                    Toast.makeText(context, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                }
                            });
                }
            });

            // 보관함 listener
            ((ItemViewHolder) holder).keep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serviceApi.Keep(LoginSharedPreference.getUserId(context),postId)
                            .enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                    int keep = item.getKeepOnset();
                                    int resultCode = response.body().getCode();
                                    if( resultCode == statusCode.RESULT_OK){
                                        keep = (keep==1)? 0 : 1;

                                        item.setKeepOnset(keep);
                                        notifyItemChanged(position);
                                        if( keep == 1)
                                            Toast.makeText(context, "보관함에 저장 되었습니다", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(context, "보관함에서 삭제 되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                    else if( resultCode == statusCode.RESULT_CLIENT_ERR){
                                        Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else if( resultCode == statusCode.RESULT_SERVER_ERR){
                                        Toast.makeText(context, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CodeResponse> call, Throwable t) {
                                    Toast.makeText(context, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                                }
                            });
                }
            });


            View.OnClickListener goPostActivity = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostActivity.class);
                    // 게시글 id 전달
                    intent.putExtra("post_id", postId);
                    context.startActivity(intent);
                }
            };
            View.OnClickListener goProfile = new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    // 게시글 사용자 id 전달
                    intent.putExtra("userId", item.getUser().getLogin_id());
                    context.startActivity(intent);
                }
            };

            ((ItemViewHolder) holder).comment.setOnClickListener(goPostActivity);
            ((ItemViewHolder) holder).picture.setOnClickListener(goPostActivity);
            ((ItemViewHolder) holder).text.setOnClickListener(goPostActivity);

            ((ItemViewHolder) holder).user_id.setOnClickListener(goProfile);
            ((ItemViewHolder) holder).profile_image.setOnClickListener(goProfile);


        }
        else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    private void showLoadingView(LoadingViewHolder holder, int position) {

    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setItem(@NonNull ItemViewHolder holder, int position){

        Post post = items.get(position).getPost();
        User user = items.get(position).getUser();

        // 받아온 데이터로 게시글 내용 셋팅
        String text = post.getText();
        if(text.length() > 15) {// text가 15자 이상일 때
            text = text.substring(0, 15);
            text += " ...더 보기";
            SpannableString newText= new SpannableString(text);
            newText.setSpan(new ForegroundColorSpan(R.color.inkLightGrey), 15, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            newText.setSpan(new StyleSpan(Typeface.ITALIC),15, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.text.setText(newText); // 글 내용(15자 이상)
        }
        else{
            holder.text.setText(text); // 글 내용(15자 이하)
        }

        if( date.format(today).equals((post.getPost_date()))) {
            int hour = Integer.parseInt(post.getPost_time().substring(0,2));
            if( hour != rightNow.get(Calendar.HOUR_OF_DAY) ){
                holder.timestamp.setText((rightNow.get(Calendar.HOUR_OF_DAY) - hour)+"시간 전");
            }
            else {
                int min = Integer.parseInt(post.getPost_time().substring(3,5));
                if( min == rightNow.get(Calendar.MINUTE) )
                    holder.timestamp.setText("방금 게시됨");
                else
                    holder.timestamp.setText((rightNow.get(Calendar.MINUTE) - min) + "분 전");
            }
        }
        else
            holder.timestamp.setText( post.getPost_date()); //게시 날짜

        Glide.with(context).load(RetrofitClient.getBaseUrl() + post.getImage() ).into(holder.picture); // 게시물 사진
        holder.comment_counter.setText("댓글 " + items.get(position).getCommentsNum()); // 댓글 수
        holder.favorite_counter.setText("좋아요 " + items.get(position).getLikeNum()); // 좋아요 수

        holder.user_id.setText(user.getLogin_id()); // 게시자 id
        Glide.with(context).load(RetrofitClient.getBaseUrl() + user.getImg_profile()).into(holder.profile_image); // 게시자 프로필 사진


        if(items.get(position).getLikeOnset()== 0)
            holder.favorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_border));
        else
            holder.favorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite));


        if(items.get(position).getKeepOnset() == 0)
            holder.keep.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_border_24));
        else
            holder.keep.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_24));


    }

    public void addItem( List<PostFeed> posts){
        items.addAll(posts);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image;
        TextView user_id;
        TextView timestamp;
        ImageView picture;
        ImageView favorite;
        TextView favorite_counter;
        TextView comment_counter;
        TextView text;
        ImageView keep;
        ImageView comment;

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            profile_image=(ImageView)itemView.findViewById(R.id.feed_item_profile_img);
            user_id=(TextView)itemView.findViewById(R.id.feed_item_id);
            timestamp=(TextView)itemView.findViewById(R.id.feed_item_time);
            picture=(ImageView)itemView.findViewById(R.id.feed_item_pic);
            favorite_counter=(TextView)itemView.findViewById(R.id.feed_item_like_cnt);
            comment_counter=(TextView)itemView.findViewById(R.id.feed_item_com_cnt);
            text=(TextView)itemView.findViewById(R.id.feed_item_text);
            favorite = (ImageView)itemView.findViewById(R.id.feed_item_like);
            keep = (ImageView)itemView.findViewById(R.id.feed_item_keep);
            comment = (ImageView)itemView.findViewById(R.id.feed_item_com);

        }
/*
        public void setItem(String item) {
            textView.setText(item);
        }*/
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.item_progressBar);
        }
    }

}
