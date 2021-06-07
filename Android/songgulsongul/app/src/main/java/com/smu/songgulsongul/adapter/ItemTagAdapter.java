package com.smu.songgulsongul.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.responseData.ItemTag;
import com.smu.songgulsongul.server.RetrofitClient;

public class ItemTagAdapter extends RecyclerView.Adapter<ItemTagAdapter.ViewHolder> {
    Context context;
    List<ItemTag> dataList;
    int itemCnt;

    public ItemTagAdapter(Context context, List<ItemTag>  obj) {
        this.context = context;

        dataList =obj;
        itemCnt = dataList.size();
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

        final ItemTag item = dataList.get(position);

        //임시로 세팅
        Glide.with(context).load(RetrofitClient.getBaseUrl() + item.getPicture() ).into(holder.pic); // 게시물 사진

        // item tag 추가의 경우 naver api 이용한 http 이미지 링크 받아오므로 baseUrl 제거 --> 실제 아래 코드 사용
        // Glide.with(context).load(item.getPicture() ).into(holder.pic); // 게시물 사진

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(context, ItemDetailActivity.class);

                ((Activity)context).startActivityForResult(intent,1234);*/
                if(mListener != null){
                    mListener.onItemClick(v);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        int pos = getAdapterPosition();

        ImageView pic;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            pic = (ImageView)itemView.findViewById(R.id.item_tag_img);
        }
    }

    public List<ItemTag> getDataList(){
        return dataList;
    }
    //item 클릭 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v);
    }

    private ItemTagAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(ItemTagAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }
}
