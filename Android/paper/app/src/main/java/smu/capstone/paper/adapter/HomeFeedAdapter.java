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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.item.HomeFeedItem;
import smu.capstone.paper.item.HomeMarketItem;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    Context context;
    ArrayList<HomeFeedItem> items = new ArrayList<HomeFeedItem>();
    ArrayList<HomeFeedItem> iconInfo = new ArrayList<HomeFeedItem>(); // 좋아요, 보관 상태에 따른 아이콘 변경 때문에 필요할듯 함,,
    JSONObject obj = new JSONObject();
    JSONArray dataList;
    int itemCnt;

    public HomeFeedAdapter (Context context, JSONObject obj) throws JSONException{
        this.context = context;
        this.items = items;
        this.obj = obj;

        dataList = obj.getJSONArray("data");
        itemCnt = dataList.length();
    }

    public void setItem(@NonNull ViewHolder holder, JSONObject item, int position){
        // 받아온 데이터로 게시글 내용 셋팅
        try {
            String text = item.getString("text");
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
            holder.timestamp.setText(item.getString("timeStamp")); // 게시 시간
            holder.comment_counter.setText(item.getInt("comCnt") + ""); // 댓글 수
            holder.favorite_counter.setText(iconInfo.get(position).getFavoriteCounter() + ""); // 좋아요 수
            holder.user_id.setText(item.getString("userId")); // 게시자 id
            Glide.with(context).load(item.getInt("profileImg")).into(holder.profile_image); // 게시자 프로필 사진
            Glide.with(context).load(item.getInt("postImg")).into(holder.picture); // 게시물 사진
        } catch (JSONException e){
            e.printStackTrace();
        }
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
       try {
           final JSONObject item = dataList.getJSONObject(position);
           iconInfo.add(new HomeFeedItem(item.getInt("like"), item.getInt("likeCnt"), item.getInt("keep")));
           final int postId = item.getInt("postId");
           setItem(holder, item, position);

           if(iconInfo.get(position).getLike() == 0){
               holder.favorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_border));
           }
           else{
               holder.favorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite));
           }


           if(iconInfo.get(position).getKeep() == 0){
               holder.keep.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_border_24));
           }
           else{
               holder.keep.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_24));
           }

           holder.comment.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, PostActivity.class);

                   // 게시글 id 넘겨주기
                   intent.putExtra("postId", postId);

                   context.startActivity(intent);
               }
           });

           holder.text.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, PostActivity.class);

                   // 게시글 id 넘겨주기
                   intent.putExtra("postId", postId);

                   context.startActivity(intent);
               }
           });

       } catch (JSONException e){
           e.printStackTrace();

       }

        // 좋아요 listener
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int like = iconInfo.get(position).getLike();
                if (like == 0) {
                    // 서버에 like객체 전달 코드 작성 -> insert
                    // --------------------------------------

                    // if resultCode == 200
                    iconInfo.get(position).setLike(1);
                    iconInfo.get(position).setFavoriteCounter(iconInfo.get(position).getFavoriteCounter() + 1);
                    notifyDataSetChanged();
                } else {
                    // 서버에 like객체 전달 코드 작성 -> delete
                    // --------------------------------------

                    // if resultCode == 200
                    iconInfo.get(position).setLike(0);
                    iconInfo.get(position).setFavoriteCounter(iconInfo.get(position).getFavoriteCounter() - 1);
                    notifyDataSetChanged();
                }
                // if resultCode == 404
                // Toast.makeText(context, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();

                Log.d("TAG", "눌렸음");

            }
        });

        // 보관함 listener
        holder.keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int keeping = iconInfo.get(position).getKeep();
                if(keeping == 0){
                    // 서버에 keep객체 전달 코드 작성 -> insert
                    // ---------------------------

                    // if resultCode == 200
                    iconInfo.get(position).setKeep(1);
                    Toast.makeText(context, "보관 성공", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
                else{
                    // 서버에 keep객체 전달 코드 작성 -> delete
                    // --------------------------------------

                    // if resultCode == 200
                    iconInfo.get(position).setKeep(0);
                    Toast.makeText(context, "보관 취소", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }

                // if resultCode == 404
                // Toast.makeText(context, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();

                Log.d("TAG", "눌렸음");
            }
        });

/*       holder.comment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, PostActivity.class);

               // 게시글 id 넘겨주기

               intent.putExtra("postId", items.get(position).getPostId());

               context.startActivity(intent);
           }
       });*/

/*       holder.text.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, PostActivity.class);

               // 게시글 id 넘겨주기

               intent.putExtra("postId", items.get(position).getPostId());


               context.startActivity(intent);
           }
       });*/

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
