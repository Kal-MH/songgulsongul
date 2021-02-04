package smu.capstone.paper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class FragHomeComu extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_comu, container, false);


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
        ComuImageAdapter adapter = new ComuImageAdapter(this.getContext(),  R.layout.comu_item ,i) ;
        gridView.setAdapter(adapter);

        return view;
    }

}
