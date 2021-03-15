package smu.capstone.paper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import smu.capstone.paper.R;
import smu.capstone.paper.activity.ProfileActivity;
import smu.capstone.paper.activity.StickerDetailActivity;
import smu.capstone.paper.item.FollowItem;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
    Context context;
    JSONObject obj = new JSONObject();
    JSONArray dataList;
    int itemCnt;

    public FollowAdapter (Context context, JSONObject obj) throws JSONException {
        this.context = context;
        this.obj = obj;

        dataList = obj.getJSONArray("data");
        itemCnt = dataList.length();
    }

    // 받아온 데이터로 팔로우/팔로워 리스트 내용 셋팅
    public void setItem(@NonNull FollowAdapter.ViewHolder holder, JSONObject item){
        try {
            holder.userid.setText(item.getString("userid"));
            Glide.with(context).load(item.getInt("image")).into(holder.profile_image);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.follow_item, parent, false);
        return new FollowAdapter.ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final FollowAdapter.ViewHolder holder, final int position) {
        try {
            JSONObject item = dataList.getJSONObject(position);
            setItem(holder, item);

            holder.follow_btn.setVisibility(View.GONE);
            holder.follow_text.setVisibility(View.VISIBLE);

            if(item.getInt("flag") == 1){
                holder.follow_btn.setVisibility(View.GONE);
                holder.follow_text.setVisibility(View.VISIBLE);
            }
            else if(item.getInt("flag") == 0){
                holder.follow_btn.setVisibility(View.VISIBLE);
                holder.follow_text.setVisibility(View.GONE);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        /*if ( item.getFollowing() ){
            holder.follow_btn.setVisibility(View.GONE);
            holder.follow_text.setVisibility(View.VISIBLE);
        }
        else{
            holder.follow_btn.setVisibility(View.VISIBLE);
            holder.follow_text.setVisibility(View.GONE);
        }*/


        holder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //팔로우 액션 --> server에 처리 요청

                //if resultCode == 200
                holder.follow_btn.setVisibility(View.GONE);
                holder.follow_text.setVisibility(View.VISIBLE);
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemCnt;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_image;
        TextView userid;
        Button follow_btn;
        TextView follow_text;
        boolean following;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            profile_image=(ImageView)itemView.findViewById(R.id.follow_item_img);
            userid = (TextView)itemView.findViewById(R.id.follow_item_id);
            follow_btn = (Button)itemView.findViewById(R.id.follow_item_btn);
            follow_text = (TextView)itemView.findViewById(R.id.follow_item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    // 클릭한 사용자의 id 전달
                    try {
                        JSONObject item = dataList.getJSONObject(pos);
                        if (pos != RecyclerView.NO_POSITION) {
                            Intent intent = new Intent(context, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("userid", item.getString("userid"));
                            context.startActivity(intent);
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }



        public void setItem(Object item){

        }

    }
}
