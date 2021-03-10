package smu.capstone.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.item.PostItem;


public class PostImageAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<PostItem> items;
    JSONObject obj = new JSONObject();
    JSONArray dataList;
    LayoutInflater inf;
    int itemCnt;
    int layout;

    public PostImageAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public PostImageAdapter(Context mContext, int layout, JSONObject obj) throws JSONException{
        this.mContext = mContext;
        inf =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.obj = obj;
        dataList = obj.getJSONArray("data");
        itemCnt = dataList.length();
    }

    //삭제할 코드 --> 컴파일용 임시로 둠
    public PostImageAdapter(Context mContext, int layout, ArrayList<PostItem> items) {
        this.mContext = mContext;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.items = items;
    }

    // 받아온 데이터로 게시글 내용 셋팅
    public void setItem(ImageView imageView, JSONObject item){
        try {
            Glide.with(mContext).load(item.getInt("postImage")).into(imageView); // 게시물 사진
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new GridView.LayoutParams(340, 350));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    //삭제할 코드 --> 컴파일용 임시로 둠
    public void setItem2(ImageView imageView,  PostItem item){
        Glide.with(mContext).load(item.getImg()).into(imageView); // 게시물 사진
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new GridView.LayoutParams(340, 350));
    }

    @Override
    public int getCount() {
        return itemCnt;
    }

    @Override
    public Object getItem(int position) {
        JSONObject item = new JSONObject();
        try {
            item = dataList.getJSONObject(position);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = new JSONObject();
        try {
            item = dataList.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        ImageView imageView = convertView.findViewById(R.id.post_image_iv);
        setItem(imageView, item);

        return imageView;
    }

}
