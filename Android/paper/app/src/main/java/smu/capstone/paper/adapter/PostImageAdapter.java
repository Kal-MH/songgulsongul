package smu.capstone.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import smu.capstone.paper.R;
import smu.capstone.paper.responseData.Post;
import smu.capstone.paper.server.RetrofitClient;


public class PostImageAdapter extends BaseAdapter {

    private Context mContext;
    List<Post> dataList;
    LayoutInflater inf;
    int itemCnt;
    int layout;

    public PostImageAdapter(Context mContext, int layout, List<Post> dataList){
        this.mContext = mContext;
        inf =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.dataList = dataList;
        itemCnt = dataList.size();
    }

    // 받아온 데이터로 게시글 내용 셋팅
    public void setItem(ImageView imageView, Post item){
            Glide.with(mContext).load(RetrofitClient.getBaseUrl()+item.getImage())
                    .into(imageView); // 게시물 사진
    }

    @Override
    public int getCount() {
        return itemCnt;
    }

    @Override
    public Object getItem(int position) {
        return  dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = (Post)dataList.get(position);

        if (convertView == null)
            convertView = inf.inflate(layout, null);

        ImageView imageView = convertView.findViewById(R.id.post_image_iv);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
        setItem(imageView, post);
        return imageView;
    }

}
