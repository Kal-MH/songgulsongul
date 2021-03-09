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
            obj.put("userId", "wonhee");
            obj.put("timeStamp", "21-02-07");
            obj.put("likeCnt", 499);
            obj.put("comCnt", 204);
            obj.put("text", "hi everyone");
            obj.put("profileImg",R.drawable.ic_baseline_emoji_emotions_24);
            obj.put("postImg",R.drawable.sampleimg);
            obj.put("like", 0);
            obj.put("keep",0);
            arr.put(obj);

            JSONObject obj2 = new JSONObject();
            obj2.put("userId", "YUJIN");
            obj2.put("timeStamp", "21-02-07");
            obj2.put("likeCnt", 20);
            obj2.put("comCnt", 52);
            obj2.put("text", "바쁘다 바빠 현대사회에 사는 이유진의 그림입니다~후후");
            obj2.put("profileImg", R.drawable.sampleimg);
            obj2.put("postImg", R.drawable.test);
            obj2.put("like", 0);
            obj2.put("keep", 0);
            arr.put(obj2);

            item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }
}
