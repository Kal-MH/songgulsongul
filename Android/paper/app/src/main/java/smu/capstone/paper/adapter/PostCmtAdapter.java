package smu.capstone.paper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.activity.ProfileActivity;

public class PostCmtAdapter extends BaseAdapter {

    ViewHolder holder;
    Context context;
    JsonArray dataList;
    int cmtCnt;


    public PostCmtAdapter (Context context, JsonArray obj)  {
        this.context = context;
        dataList = obj;
        cmtCnt = dataList.size();
    }
    @Override public int getCount() {
        return dataList.size();
    }
    @Override public View getView(int position, View convertView, ViewGroup parent) {

        int pos = position;
        final Context context = parent.getContext(); // "listview_item" Layout을 inflate하여 convertView 참조 획득.

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_cmt_item, parent, false);

            holder = new ViewHolder(convertView);

            // 해당 View에 setTag로 Holder 객체 저장
            convertView.setTag(holder);
        } // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        else {
            // ConvertView가 이미 생성된적 있다면, 저장되어있는 Holder 가져오기
            holder = (ViewHolder) convertView.getTag();
        }

        final JsonObject item = dataList.get(position).getAsJsonObject();
        holder.userId.setText(item.get("login_id").getAsString());
        holder.cmt.setText(item.get("text").getAsString());

        holder.userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                // 게시글 사용자 id 전달
                intent.putExtra("userId",item.get("login_id").getAsString());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
    @Override public long getItemId(int position) {
        return dataList.get(position).getAsJsonObject().get("id").getAsLong();
    }

    @Override public Object getItem(int position) {
        return dataList.get(position).getAsJsonObject();
    }

    static class ViewHolder{
        TextView userId;
        TextView cmt;
        public ViewHolder(View convertView) {
            userId = (TextView) convertView.findViewById(R.id.post_item_id);
            cmt = (TextView) convertView.findViewById(R.id.post_item_cmt);
        }
    }

}
