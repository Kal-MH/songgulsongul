package smu.capstone.paper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.activity.AddItemtagActivity;
import smu.capstone.paper.activity.ItemDetailActivity;
import smu.capstone.paper.activity.PostEditActivity;
import smu.capstone.paper.activity.UploadDetailActivity;
import smu.capstone.paper.item.ItemtagItem;



public class AddItemTagAdapter extends ItemTagAdapter {
    public AddItemTagAdapter(Context context, JSONObject obj) throws JSONException {
        super(context, obj);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        try {
            final JSONObject item = dataList.getJSONObject(position);
            holder.pic.setImageBitmap((Bitmap) item.get("Image"));
        } catch (JSONException e){
            e.printStackTrace();

        }

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    Log.d("TAG","add action");
                    Intent intent = new Intent(context, AddItemtagActivity.class);
                    ((Activity) context).startActivityForResult(intent,100);
                }
            }
        });
    }
}
