package smu.capstone.paper.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.FollowAdapter;
import smu.capstone.paper.adapter.HomeFeedAdapter;
import smu.capstone.paper.item.FollowItem;
import smu.capstone.paper.item.HomeFeedItem;

public class FragFollowing extends Fragment {

    RecyclerView rv;
    FollowAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_following, container, false);

        rv = rootView.findViewById(R.id.follow_following_rv);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        adapter = new FollowAdapter(getContext());

        //임시 데이터 저장
        FollowItem data = new FollowItem(false,"Prof.cho", drawable2Bitmap(getResources().getDrawable(R.drawable.test)));
        adapter.insertItem(data);
        FollowItem data1 = new FollowItem(false,"yujin1292", drawable2Bitmap(getResources().getDrawable(R.drawable.ic_baseline_emoji_emotions_24)));
        adapter.insertItem(data1);
        FollowItem data2 = new FollowItem(true,"arami98", drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)));
        adapter.insertItem(data2);
        FollowItem data3 = new FollowItem(false,"wonhee123", drawable2Bitmap(getResources().getDrawable(R.drawable.ic_chat_black)));
        adapter.insertItem(data3);



        rv.setAdapter(adapter);

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