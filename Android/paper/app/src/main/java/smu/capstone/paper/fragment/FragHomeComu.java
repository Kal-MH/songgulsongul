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
        ArrayList<PostItem> items = getPostData();

        // 어뎁터 적용
        PostImageAdapter adapter = new PostImageAdapter(this.getContext(),  R.layout.post_image_item , items ) ;

        gridView.setAdapter(adapter);

        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // 서버에 게시글 id 전달
                //---------------------

                //if resultCode == 200
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);

                //if resultCode == 404
                //Toast.makeText(context, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();

                Log.d("TAG", position + "is Clicked");      // Can not getting this method.
            }
        });



        return view;
    }

    //server에서 전달한 data로 postitem객체 초기화 (반복수행)
    public ArrayList<PostItem> getPostData(){
        ArrayList<PostItem> items = new ArrayList<PostItem>();

        //임시 데이터 저장
        PostItem data = new PostItem(R.drawable.sampleimg);
        items.add(data);
        PostItem data1 = new PostItem(R.drawable.sampleimg);
        items.add(data1);
        PostItem data2 = new PostItem(R.drawable.sampleimg);
        items.add(data2);
        PostItem data3 = new PostItem(R.drawable.sampleimg);
        items.add(data3);
        PostItem data4 = new PostItem(R.drawable.sampleimg);
        items.add(data4);
        PostItem data5 = new PostItem(R.drawable.sampleimg);
        items.add(data5);
        PostItem data6 = new PostItem(R.drawable.sampleimg);
        items.add(data6);
        PostItem data7 = new PostItem(R.drawable.sampleimg);
        items.add(data7);
        PostItem data8 = new PostItem(R.drawable.sampleimg);
        items.add(data8);
        PostItem data9 = new PostItem(R.drawable.sampleimg);
        items.add(data9);
        PostItem data10 = new PostItem(R.drawable.sampleimg);
        items.add(data10);
        PostItem data11 = new PostItem(R.drawable.sampleimg);
        items.add(data11);
        PostItem data12 = new PostItem(R.drawable.sampleimg);
        items.add(data12);
        PostItem data13 = new PostItem(R.drawable.sampleimg);
        items.add(data13);

        return items;
    }

}
