package smu.capstone.paper.adapter;

import android.content.Context;
import android.os.Build;
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
import smu.capstone.paper.item.HashtagItem;
import smu.capstone.paper.item.ItemtagItem;

public class HashTagAdapter extends RecyclerView.Adapter<HashTagAdapter.ViewHolder>{
    Context context;
    ArrayList<HashtagItem> items = new ArrayList<HashtagItem>();

    public HashTagAdapter(Context context){
        this.context = context;
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
        HashtagItem item = items.get(position);
        holder.content.setText(item.getContent());

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void insertItem(HashtagItem data){
        items.add(data);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView content;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            content = (TextView)itemView.findViewById(R.id.hash_tag_content);

        }


        public void setItem(Object item){

        }

    }
}
