package smu.capstone.paper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
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

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.item.HomeFeedItem;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    Context context;
    ArrayList<HomeFeedItem> items = new ArrayList<HomeFeedItem>();


    public HomeFeedAdapter (Context context, ArrayList<HomeFeedItem> items){
        this.context = context;
        this.items = items;
    }

    public void setItem(@NonNull ViewHolder holder, HomeFeedItem item){
        // 받아온 데이터로 게시글 내용 셋팅
        holder.text.setText(item.getText()); // 글 내용
        holder.timestamp.setText(item.getTimeStamp()); // 게시 시간
        holder.comment_counter.setText(item.getCommentCounter()+""); // 댓글 수
        holder.favorite_counter.setText(item.getFavoriteCounter()+""); // 좋아요 수
        holder.user_id.setText(item.getUserId()); // 게시자 id
        Glide.with(context).load(item.getProfile_image()).into(holder.profile_image); // 게시자 프로필 사진
        Glide.with(context).load(item.getPicture()).into(holder.picture); // 게시물 사진
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
       HomeFeedItem item = items.get(position);
       setItem(holder, item);

       if(items.get(position).getLike() == 0){
           holder.favorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_border));
       }
       else{
           holder.favorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite));
       }

       if(items.get(position).getKeep() == 0){
           holder.keep.setImageDrawable(context.getDrawable(R.drawable.baseline_bookmark_border_black_18dp));
       }
       else{
           holder.keep.setImageDrawable(context.getDrawable(R.drawable.baseline_bookmark_black_18dp));
       }

      holder.favorite.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              int like = items.get(position).getLike();

              if(like == 0) {
                  // 서버에 like객체 전달 코드 작성 -> insert
                  // --------------------------------------

                  // if resultCode == 200
                  items.get(position).setLike(1);
                  items.get(position).setFavoriteCounter( items.get(position).getFavoriteCounter() + 1);
                  notifyDataSetChanged();
              }
              else {
                  // 서버에 like객체 전달 코드 작성 -> delete
                  // --------------------------------------

                  // if resultCode == 200
                  items.get(position).setLike(0);
                  items.get(position).setFavoriteCounter( items.get(position).getFavoriteCounter() - 1);
                  notifyDataSetChanged();
              }

              // if resultCode == 404
              // Toast.makeText(context, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();

              Log.d("TAG", "눌렸음");

          }
      });

       // 보관함에 보관
       holder.keep.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int keeping = items.get(position).getKeep();

               if(keeping == 0){
                   // 서버에 keep객체 전달 코드 작성 -> insert
                   // ---------------------------

                   // if resultCode == 200
                   items.get(position).setKeep(1);
                   Toast.makeText(context, "보관 성공", Toast.LENGTH_SHORT).show();
                   notifyDataSetChanged();
               }
               else{
                   // 서버에 keep객체 전달 코드 작성 -> delete
                   // --------------------------------------

                   // if resultCode == 200
                   items.get(position).setKeep(0);
                   Toast.makeText(context, "보관 취소", Toast.LENGTH_SHORT).show();
                   notifyDataSetChanged();
               }

               // if resultCode == 404
               // Toast.makeText(context, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();

               Log.d("TAG", "눌렸음");
           }
       });

       holder.comment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, PostActivity.class);

               // 게시글 id 넘겨주기
               //intent.putExtra("postId", items.get(position).getPostId());

               context.startActivity(intent);
           }
       });

       holder.more.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, PostActivity.class);

               // 게시글 id 넘겨주기
               //intent.putExtra("postId", items.get(position).getPostId());

               context.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void insertItem(HomeFeedItem data){
        items.add(data);
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
        TextView more;

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
            more = (TextView)itemView.findViewById(R.id.feed_item_more);


        }
    }
}
