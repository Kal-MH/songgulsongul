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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.FollowAdapter;
import smu.capstone.paper.adapter.HomeFeedAdapter;
import smu.capstone.paper.item.FollowItem;
import smu.capstone.paper.item.HomeFeedItem;

public class FragFollowing extends Fragment {

    RecyclerView rv;
    FollowAdapter adapter;
    int status = 1;
    JSONObject login_following_list = getFollowingData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_following, container, false);

        rv = rootView.findViewById(R.id.follow_following_rv);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        JSONObject obj = getFollowingData();
        try {
            if(status == 1)
                adapter = new FollowAdapter(getContext(), obj);
            else
                adapter = new FollowAdapter(getContext(), getUserFollowingData());
        } catch (JSONException e){
            e.printStackTrace();
        }

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

    // server에서 data전달 --> 로그인한 사용자의 팔로우 리스트
    public JSONObject getFollowingData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();

        // 임시 데이터 저장
        try{
            JSONObject obj1 = new JSONObject();
            obj1.put("userId", "Prof.cho");
            obj1.put("image", R.drawable.test);
            arr.put(obj1);

            JSONObject obj2 = new JSONObject();
            obj2.put("userId", "yujin1292");
            obj2.put("image", R.drawable.ic_baseline_emoji_emotions_24);
            arr.put(obj2);

            JSONObject obj3 = new JSONObject();
            obj3.put("userId", "arami98");
            obj3.put("image", R.drawable.ic_favorite);
            arr.put(obj3);

            JSONObject obj4 = new JSONObject();
            obj4.put("userId", "wonhee123");
            obj4.put("image", R.drawable.ic_chat_black);
            arr.put(obj4);
            item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return item;
    }

    // server에서 data전달 --> 로그인한 사용자가 아닐경우 (로그인한 사용자의 리스트 + 선택한 사용자의 리스트 전달)
    public JSONObject getUserFollowingData(){
        JSONObject item = new JSONObject();
        JSONArray user_arr = new JSONArray();

        try{
            JSONArray arr = getFollowingData().getJSONArray("data");

            // 임시 데이터 저장
            JSONObject user_obj1 = new JSONObject();
            user_obj1.put("userId", "yujin1292");
            user_obj1.put("image", R.drawable.ic_baseline_emoji_emotions_24);
            user_arr.put(user_obj1);

            JSONObject user_obj2 = new JSONObject();
            user_obj2.put("userId", "arami98");
            user_obj2.put("image", R.drawable.ic_favorite);
            user_arr.put(user_obj2);

            JSONObject user_obj3 = new JSONObject();
            user_obj3.put("userId", "wonhee123");
            user_obj3.put("image", R.drawable.ic_chat_black);
            user_arr.put(user_obj3);

            JSONObject user_obj4 = new JSONObject();
            user_obj4.put("userId", "hahahoho");
            user_obj4.put("image", R.drawable.ic_baseline_face_24);
            user_arr.put(user_obj4);

            // 선택한 사용자의 팔로우 리스트에 있는 사용자를 팔로우 했는지 체크
            for(int i = 0; i < user_arr.length(); i++){
                for(int j = 0; j < arr.length(); j++){
                    int check = 0;
                    String user_follower_id = user_arr.getJSONObject(i).getString("userId");
                    String follower_id = arr.getJSONObject(j).getString("userId");

                    // Following
                    if(user_follower_id == follower_id) {
                        user_arr.getJSONObject(i).put("flag", 1);
                        check = 1;
                        break;
                    }

                    // Unfollowing
                    if (check == 0)
                        user_arr.getJSONObject(i).put("flag", 0);
                }
            }
            item.put("data", user_arr);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return item;
    }

}