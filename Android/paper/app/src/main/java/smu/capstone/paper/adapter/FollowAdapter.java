package smu.capstone.paper.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import smu.capstone.paper.R;
import smu.capstone.paper.item.FollowItem;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
    Context context;
    ArrayList<FollowItem> items = new ArrayList<FollowItem>();


    public FollowAdapter (Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.follow_item, parent, false);
        return new FollowAdapter.ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull FollowAdapter.ViewHolder holder, final int position) {
        FollowItem item = items.get(position);
        holder.userid.setText(item.getId());
        holder.profile_image.setImageBitmap(item.getImage());


        if ( item.getFollowing() ){
            holder.follow_btn.setVisibility(View.GONE);
            holder.follow_text.setVisibility(View.VISIBLE);
        }
        else{
            holder.follow_btn.setVisibility(View.VISIBLE);
            holder.follow_text.setVisibility(View.GONE);
        }


        holder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //팔로우 액션


            }
        });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void insertItem(FollowItem data){
        items.add(data);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_image;
        TextView userid;
        Button follow_btn;
        TextView follow_text;
        boolean following;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            profile_image=(ImageView)itemView.findViewById(R.id.follow_item_img);
            userid = (TextView)itemView.findViewById(R.id.follow_item_id);
            follow_btn = (Button)itemView.findViewById(R.id.follow_item_btn);
            follow_text = (TextView)itemView.findViewById(R.id.follow_item_text);
        }



        public void setItem(Object item){

        }

    }
}
