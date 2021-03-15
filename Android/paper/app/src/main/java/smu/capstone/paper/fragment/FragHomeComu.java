package smu.capstone.paper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.activity.PostSearchActivity;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.item.HomeFeedItem;
import smu.capstone.paper.item.PostItem;

public class FragHomeComu extends Fragment {
    private View view;
    private SearchView searchView;
    PostImageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_comu, container, false);

        searchView = view.findViewById(R.id.comu_search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // 검색 버튼 눌렀을 시 발생
                // 서버에 query객체 전달 코드 작성
                // ----------------------------

                // if resultCode == 200
                Intent intent = new Intent(getActivity(), PostSearchActivity.class);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { // 검색어 입력 시 발생
                return false;
            }
        });

        // view에서 id 찾아야함
        GridView gridView = view.findViewById(R.id.comu_grid);
        final JSONObject obj = getPostData();

        // 어뎁터 적용
        try {
            adapter = new PostImageAdapter(this.getContext(), R.layout.post_image_item, obj);
        } catch (JSONException e){
            e.printStackTrace();
        }
        gridView.setAdapter(adapter);


        //Click Listener
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

    //server에서 data전달
    public JSONObject getPostData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();
        int pid = 1;

        //임시 데이터 저장
        try{
            for(int i = 0; i < 10; i++){
                JSONObject obj = new JSONObject();
                obj.put("postImage", R.drawable.sampleimg);
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
