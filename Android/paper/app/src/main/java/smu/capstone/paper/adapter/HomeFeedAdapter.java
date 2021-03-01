package smu.capstone.paper.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.item.HomeFeedItem;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    Context context;
    ArrayList<HomeFeedItem> items = new ArrayList<HomeFeedItem>();


    public HomeFeedAdapter (Context context){
        this.context = context;
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
       holder.text.setText(item.getText());
       holder.timestamp.setText(item.getTimeStamp());
       holder.comment_counter.setText(item.getCommentCounter()+"");
       holder.favorite_counter.setText(item.getFavoriteCounter()+"");
       holder.user_id.setText(item.getUserId());

       holder.profile_image.setImageBitmap(item.getProfile_image());
       holder.picture.setImageBitmap(item.getPicture());

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
                  items.get(position).setLike(1);
                  items.get(position).setFavoriteCounter( items.get(position).getFavoriteCounter() + 1);
              }
              else {
                  items.get(position).setLike(0);
                  items.get(position).setFavoriteCounter( items.get(position).getFavoriteCounter() - 1);
              }

              notifyDataSetChanged();
              Log.d("TAG", "눌렸음");

          }
      });

       holder.keep.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int keeping = items.get(position).getKeep();
               if(keeping == 0){
                   items.get(position).setKeep(1);
               }
               else{
                   items.get(position).setKeep(0);
               }

               notifyDataSetChanged();
               Log.d("TAG", "눌렸음");
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



        }



        public void setItem(Object item){

        }

    }
}
