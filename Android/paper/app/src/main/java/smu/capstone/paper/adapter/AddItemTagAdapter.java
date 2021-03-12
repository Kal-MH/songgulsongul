package smu.capstone.paper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.activity.PostEditActivity;
import smu.capstone.paper.item.ItemtagItem;



public class AddItemTagAdapter extends ItemTagAdapter {
    public AddItemTagAdapter(Context context, JSONObject obj) throws JSONException {
        super(context, obj);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ItemtagItem item = items.get(position);
        holder.pic.setImageBitmap(item.getPic());

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                    Log.d("TAG","add action");
                }
            }
        });

    }

}
