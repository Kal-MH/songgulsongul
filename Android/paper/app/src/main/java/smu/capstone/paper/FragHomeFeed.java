package smu.capstone.paper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import smu.capstone.paper.data.FeedData;

public class FragHomeFeed extends Fragment {
    RecyclerView recyclerView;
    HomeFeedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_home_feed, container, false);
        recyclerView = rootView.findViewById(R.id.feed_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HomeFeedAdapter(getContext());

        //임시 데이터 저장
        FeedData data = new FeedData("wonhee","21-02-07",499,204,
                "hi everyone",0,
                drawable2Bitmap(getResources().getDrawable(R.drawable.ic_baseline_emoji_emotions_24)),
                drawable2Bitmap(getResources().getDrawable(R.drawable.sampleimg)), 0);
        adapter.insertItem(data);

        FeedData data1 = new FeedData("YUJIN","21-02-07",20,52,
                "너무멋지다!~",0,
                drawable2Bitmap(getResources().getDrawable(R.drawable.sampleimg)),
                drawable2Bitmap(getResources().getDrawable(R.drawable.test)), 0);
        adapter.insertItem(data1);


        recyclerView.setAdapter(adapter);

        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
