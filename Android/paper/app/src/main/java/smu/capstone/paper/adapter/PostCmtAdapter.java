package smu.capstone.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.item.PostCmtItem;

public class PostCmtAdapter extends BaseAdapter {
    private TextView post_item_id;
    private TextView post_item_cmt;
    Context context;
    JSONObject obj = new JSONObject();
    JSONArray dataList;
    LayoutInflater inf;
    int itemCnt;
    int layout;

    ArrayList<PostCmtItem> items;

    public PostCmtAdapter (Context context, JSONObject obj) throws JSONException {
        this.context = context;
        //this.items = items;
        this.obj = obj;

        dataList = obj.getJSONArray("data");
        itemCnt = dataList.length();
    }


    public void setItem(@NonNull PostCmtAdapter.ViewHolder holder,JSONObject item, int position){
        // 받아온 데이터로 셋팅
        try {
            holder.user_id.setText(item.getString("user_id"));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override public int getCount() {
        return items.size();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final int pos = position;
        final Context context = parent.getContext(); // "listview_item" Layout을 inflate하여 convertView 참조 획득.

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_cmt_item, parent, false);
            holder = new ViewHolder();
            holder.user_id = convertView.findViewById(R.id.post_item_id);
            holder.cmt = convertView.findViewById(R.id.post_item_cmt);
            // 해당 View에 setTag로 Holder 객체 저장
            convertView.setTag(holder);
        } // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        else {
            // ConvertView가 이미 생성된적 있다면, 저장되어있는 Holder 가져오기
            holder = (ViewHolder) convertView.getTag();
        }

        PostCmtItem postcmtitem = items.get(position); // 아이템 내 각 위젯에 데이터 반영

        holder.user_id.setText(postcmtitem.getId());
        holder.cmt.setText(postcmtitem.getCmt());

        return convertView;
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public Object getItem(int position) {
        return items.get(position);
    }

    static class ViewHolder{
        TextView user_id;
        TextView cmt;
    }

    public void addItem(String id, String cmt) {
        PostCmtItem item = new PostCmtItem();
        item.setId(id);
        item.setCmt(cmt);
        items.add(item);
    }
}
