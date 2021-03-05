package smu.capstone.paper.fragment;

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

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.item.PostItem;

public class FragPostAccount extends Fragment {

    private View view;
    ArrayList<PostItem> items = new ArrayList<PostItem>();

    public void addItem(PostItem item){
        items.add(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_post_account, container, false);

        GridView gridView = view.findViewById(R.id.frag_account_grid);

        //임시 데이터 저장 --> server에서 전달한 data로 postitem객체 초기화 (반복수행)
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));
        addItem(new PostItem(R.drawable.sampleimg));

        PostImageAdapter adapter = new PostImageAdapter(this.getContext(),  R.layout.post_image_item , items ) ;
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Log.d("TAG", position + "is Clicked");      // Can not getting this method.

            }
        });

        return view;
    }
}
