package smu.capstone.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.item.PostCmtItem;

public class PostCmtAdapter extends BaseAdapter {
    private TextView post_item_id;
    private TextView post_item_cmt;

    private ArrayList<PostCmtItem> items = new ArrayList<PostCmtItem>();

    public PostCmtAdapter(){

    }

    @Override public int getCount() {
        return items.size();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext(); // "listview_item" Layout을 inflate하여 convertView 참조 획득.

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_cmt_item, parent, false);
        } // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        post_item_id = (TextView) convertView.findViewById(R.id.post_item_id);
        post_item_cmt = (TextView) convertView.findViewById(R.id.post_item_cmt);

        PostCmtItem postcmtitem = items.get(position); // 아이템 내 각 위젯에 데이터 반영

        post_item_id.setText(postcmtitem.getId());
        post_item_cmt.setText(postcmtitem.getCmt());

        return convertView;
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public Object getItem(int position) {
        return items.get(position);
    }

    public void addItem(String id, String cmt) {
        PostCmtItem item = new PostCmtItem();
        item.setId(id);
        item.setCmt(cmt);
        items.add(item);
    }
}
