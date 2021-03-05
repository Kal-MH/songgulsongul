package smu.capstone.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.item.GalleryItem;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<GalleryItem> galleryItems;
    private Context context;
    private OnItemsClickListener listener;

    public GalleryAdapter(Context context, ArrayList<GalleryItem> galleryItems) {
        this.context = context;
        this.galleryItems = galleryItems;
    }


    public void setWhenClickListener(OnItemsClickListener listener){
        this.listener = listener;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final GalleryItem galleryItem = galleryItems.get(i);
        Glide.with(context).load(galleryItem.getPath()).into(viewHolder.img);

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(galleryItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.gal_item_img);
        }


    }

    // Interface to perform events on Main-List item click
    public interface OnItemsClickListener{
        void onItemClick(GalleryItem galleryItem);
    }


}