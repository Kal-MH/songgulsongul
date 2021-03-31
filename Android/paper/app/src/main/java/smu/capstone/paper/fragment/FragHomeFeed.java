package smu.capstone.paper.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.jar.JarException;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.HomeFeedAdapter;
import smu.capstone.paper.item.HomeFeedItem;

public class FragHomeFeed extends Fragment {
    RecyclerView recyclerView;
    HomeFeedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_home_feed, container, false);

        JSONObject obj = GetFeedData();
        recyclerView = rootView.findViewById(R.id.feed_recycler);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        try {
            adapter = new HomeFeedAdapter(getContext(), obj);
        } catch (JSONException e){
            e.printStackTrace();
        }
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

    // server에서 data전달
    public JSONObject GetFeedData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();

        // 임시 데이터 저장
        try{
            JSONObject obj = new JSONObject();
            obj.put("user_id", "wonhee");
            obj.put("post_time", "21-02-07");
            obj.put("likeNum", 499);
            obj.put("commentsNum", 204);
            obj.put("text", "hi everyone");
            obj.put("img_profile",R.drawable.ic_baseline_emoji_emotions_24);
            obj.put("image",R.drawable.sampleimg);
            obj.put("likeOnset", 0);
            obj.put("keepOnset",0);
            obj.put("post_id", 1);
            arr.put(obj);

            JSONObject obj2 = new JSONObject();
            obj2.put("user_id", "YUJIN");
            obj2.put("post_time", "21-02-07");
            obj2.put("likeNum", 20);
            obj2.put("commentsNum", 52);
            obj2.put("text", "바쁘다 바빠 현대사회에 사는 이유진의 그림입니다~후후");
            obj2.put("img_profile", R.drawable.sampleimg);
            obj2.put("image", R.drawable.test);
            obj2.put("likeOnset", 0);
            obj2.put("keepOnset", 0);
            obj2.put("post_id",2);
            arr.put(obj2);

            item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }
}
