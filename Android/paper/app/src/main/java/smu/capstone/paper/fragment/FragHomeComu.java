package smu.capstone.paper.fragment;

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

import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.R;

public class FragHomeComu extends Fragment {
    private View view;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_comu, container, false);

        searchView = view.findViewById(R.id.comu_search);

        // view에서 id 찾아야함
        GridView gridView = view.findViewById(R.id.comu_grid);

        //아이템 추가
        int[] i = {R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg,
                R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg,
                R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg,
                R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg,
                R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg,R.drawable.sampleimg
        };

        // 어뎁터 적용
        PostImageAdapter adapter = new PostImageAdapter(this.getContext(),  R.layout.post_image_item , i ) ;

        gridView.setAdapter(adapter);

        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Log.d("TAG", position + "is Clicked");      // Can not getting this method.

            }
        });



        return view;
    }

}
