package smu.capstone.paper.adapter;

import android.content.Context;
import android.content.Intent;
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
import smu.capstone.paper.activity.StickerDetailActivity;
import smu.capstone.paper.item.StickerItem;

public class StickerSearchAdapter extends RecyclerView.Adapter<StickerSearchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StickerItem> items;

    public StickerSearchAdapter(Context context, ArrayList<StickerItem> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public StickerSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.sticker_search_item, parent, false);
        return new StickerSearchAdapter.ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull StickerSearchAdapter.ViewHolder holder, final int position) {
        StickerItem item = items.get(position);
        holder.sticker_price.setText(item.getSprice());
        holder.sticker_name.setText(item.getSname());
        holder.sticker_image.setImageResource(item.getImg());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sticker_image;
        TextView sticker_name;
        TextView sticker_price;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            sticker_image = (ImageView)itemView.findViewById(R.id.stk_sitem_image);
            sticker_name = (TextView)itemView.findViewById(R.id.stk_sitem_name);
            sticker_price = (TextView)itemView.findViewById(R.id.stk_sitem_price);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, StickerDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("image", items.get(pos).getImg());
                        intent.putExtra("name", items.get(pos).getSname());
                        intent.putExtra("price", items.get(pos).getSprice());

                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
