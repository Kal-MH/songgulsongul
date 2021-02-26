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
import smu.capstone.paper.item.ItemtagItem;

public class ItemTagAdaptor extends RecyclerView.Adapter<ItemTagAdaptor.ViewHolder> {
    Context context;
    ArrayList<ItemtagItem> items = new ArrayList<ItemtagItem>();


    public ItemTagAdaptor (Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.itemtag_item, parent, false);
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ItemtagItem item = items.get(position);
        holder.pic.setImageBitmap(item.getPic());

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position==0){
                    // 아이템 태그 추가
                    Log.d("TAG", "아이템태그 추가");
                }
                else{
                    Log.d("TAG", position+"");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void insertItem(ItemtagItem data){
        items.add(data);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView pic;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            pic = (ImageView)itemView.findViewById(R.id.item_tag_img);

        }


        public void setItem(Object item){

        }

    }
}
