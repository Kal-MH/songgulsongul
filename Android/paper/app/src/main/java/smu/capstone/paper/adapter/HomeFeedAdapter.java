package smu.capstone.paper.adapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.activity.ProfileActivity;
import smu.capstone.paper.data.CodeResponse;
import smu.capstone.paper.data.Post;
import smu.capstone.paper.data.User;
import smu.capstone.paper.item.PostComu;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    Context context;
    List<PostComu> postComuList;
    int itemCnt;
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;

    public HomeFeedAdapter (Context context, List<PostComu> postComuList)  {
        this.context = context;
        this.postComuList = postComuList;
        itemCnt = postComuList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setItem(@NonNull ViewHolder holder, int position){

        Post post = postComuList.get(position).getPost();
        User user = postComuList.get(position).getUser();

        // 받아온 데이터로 게시글 내용 셋팅
        String text = post.getText();
        if(text.length() > 15) {// text가 15자 이상일 때
            text = text.substring(0, 15);
            text += " ...더 보기";
            SpannableString newText= new SpannableString(text);
            newText.setSpan(new ForegroundColorSpan(Color.parseColor("#D3D3D3")), 15, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            newText.setSpan(new StyleSpan(Typeface.ITALIC),15, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.text.setText(newText); // 글 내용(15자 이상)
        }
        else{
            holder.text.setText(text); // 글 내용(15자 이하)
        }
        holder.timestamp.setText( post.getPost_time() ); // 게시 시간
        Glide.with(context).load(RetrofitClient.getBaseUrl() + post.getImage() ).into(holder.picture); // 게시물 사진
        holder.comment_counter.setText("댓글 " + postComuList.get(position).getCommentsNum()); // 댓글 수
        holder.favorite_counter.setText("좋아요 " + postComuList.get(position).getLikeNum()); // 좋아요 수

        holder.user_id.setText(user.getLogin_id()); // 게시자 id
        Glide.with(context).load(RetrofitClient.getBaseUrl() + user.getImg_profile()).into(holder.profile_image); // 게시자 프로필 사진


        if(postComuList.get(position).getLikeOnset()== 0)
            holder.favorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_border));
        else
            holder.favorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite));


        if(postComuList.get(position).getKeepOnset() == 0)
            holder.keep.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_border_24));
        else
            holder.keep.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_24));


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //post 가져옴
        final PostComu item = postComuList.get(position);
        final int postId = item.getPost().getId();


        setItem(holder, position);

        // 좋아요 listener
        holder.favorite.setOnClickListener(new View.OnClickListener() {
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
        holder.keep.setOnClickListener(new View.OnClickListener() {
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

       holder.comment.setOnClickListener(goPostActivity);
       holder.picture.setOnClickListener(goPostActivity);
       holder.text.setOnClickListener(goPostActivity);

       holder.user_id.setOnClickListener(goProfile);
       holder.profile_image.setOnClickListener(goProfile);
    }


    @Override
    public int getItemCount() {
        return itemCnt;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        int [] ImageId = {R.drawable.ic_favorite_border, R.drawable.ic_favorite};
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

        public ViewHolder(@NonNull View itemView){
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
    }
}
