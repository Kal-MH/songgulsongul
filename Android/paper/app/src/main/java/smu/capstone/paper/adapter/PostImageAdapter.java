package smu.capstone.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.item.PostItem;


public class PostImageAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<PostItem> items;
    LayoutInflater inf;
    int layout;

    public PostImageAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public PostImageAdapter(Context mContext, int layout, ArrayList<PostItem> items) {

        this.mContext = mContext;
        inf =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PostItem postItem = items.get(position);

        if (convertView==null)
            convertView = inf.inflate(layout, null);

        ImageView imageView = convertView.findViewById(R.id.post_image_iv);

        imageView.setImageResource(postItem.getImg());

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(340, 350));

        return imageView;
    }

}
