package smu.capstone.paper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.item.PostItem;

public class FragPostAccount extends Fragment {

    private View view;
    //ArrayList<PostItem> items = new ArrayList<PostItem>();
    PostImageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_post_account, container, false);

        GridView gridView = view.findViewById(R.id.frag_account_grid);

        final JSONObject obj = getAccountData();

        try {
            adapter = new PostImageAdapter(this.getContext(), R.layout.post_image_item, obj);
        } catch (JSONException e){
            e.printStackTrace();
        }
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getContext(), PostActivity.class);

                // 게시글 id 전달
                try {
                    int postId = obj.getJSONArray("data").getJSONObject(position).getInt("postId");
                    intent.putExtra("postId", postId);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                startActivity(intent);

                Log.d("TAG", position + "is Clicked");      // Can not getting this method.

            }
        });

        return view;
    }

    public JSONObject getAccountData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();
        int pid = 1;

        //임시 데이터 저장
        try{
            for(int i = 0; i < 14; i++){
                JSONObject obj = new JSONObject();
                obj.put("postImage", R.drawable.ic_favorite_border);
                obj.put("postId", pid);
                pid++;
                arr.put(obj);
            }
            item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }
}
