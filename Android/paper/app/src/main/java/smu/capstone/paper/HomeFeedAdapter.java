package smu.capstone.paper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import smu.capstone.paper.data.FeedData;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    Context context;
    ArrayList<Object> items = new ArrayList<Object>();


    public HomeFeedAdapter (Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.frag_home_feed, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       ArrayList<Object> item = (ArrayList<Object>) items.get(position);
       holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
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

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            profile_image=(ImageView)itemView.findViewById(R.id.feed_item_profile_image);
            user_id=(TextView)itemView.findViewById(R.id.feed_item_id);
            timestamp=(TextView)itemView.findViewById(R.id.feed_item_timestamp);
            picture=(ImageView)itemView.findViewById(R.id.feed_item_picture);
            favorite_counter=(TextView)itemView.findViewById(R.id.feed_item_favoritecounter);
            comment_counter=(TextView)itemView.findViewById(R.id.feed_item_commentcounter);
            text=(TextView)itemView.findViewById(R.id.feed_item_text);
            favorite = (ImageView)itemView.findViewById(R.id.feed_item_favorite);

            favorite.setImageResource(R.drawable.ic_favorite_border);

        }

        class Listener implements View.OnClickListener {
            int i=0;
            public void onClick(View v){
                favorite.setImageResource(ImageId[i]);
                i+=1;
                if(i==ImageId.length)
                    i = 0;
            }
        }

        public void setItem(Object item){

        }

    }
}
