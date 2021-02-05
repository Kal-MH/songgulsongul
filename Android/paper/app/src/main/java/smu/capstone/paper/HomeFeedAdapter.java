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

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    Context context;
    //ArrayList<HashMap<String,String>> items = new ArrayList<HashMap<String,String>>();

    public HomeFeedAdapter (Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.fragment_feed, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       //HashMap<String,String> item = items.get(position);
        //holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_image;
        TextView user_id;
        ImageView picture;
        TextView text;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            profile_image=(ImageView)itemView.findViewById(R.id.item_feed_profile_image);
            user_id=(TextView)itemView.findViewById(R.id.item_feed_id);
            picture=(ImageView)itemView.findViewById(R.id.item_feed_picture);
            text=(TextView)itemView.findViewById(R.id.item_feed_text);
        }

    }
}
