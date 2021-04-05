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
import smu.capstone.paper.item.FollowItem;

public class FragFollower extends Fragment {


    RecyclerView rv;
    FollowAdapter adapter;
    FragFollowing following = new FragFollowing();
    JSONObject followingObj =  following.login_following_list; // 로그인한 사용자의 following 리스트 가져오기
    int status = 1; // 로그인한 사용자 가정


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_follower, container, false);

        rv = rootView.findViewById(R.id.follow_follower_rv);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        JSONObject obj = getFollowerData();

        try {
            adapter = new FollowAdapter(getContext(), obj);
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

    // server에서 data전달
    public JSONObject getFollowerData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();
        JSONArray user_arr = new JSONArray();

        // 임시 데이터 저장 -- 로그인한 사용자의 팔로워 리스트
        try{
            JSONObject obj1 = new JSONObject();
            obj1.put("userId", "yujin1292");
            obj1.put("image", R.drawable.ic_baseline_emoji_emotions_24);
            arr.put(obj1);

            JSONObject obj2 = new JSONObject();
            obj2.put("userId", "arami98");
            obj2.put("image", R.drawable.ic_favorite);
            arr.put(obj2);

            JSONObject obj3 = new JSONObject();
            obj3.put("userId", "wonhee123");
            obj3.put("image", R.drawable.ic_chat_black);
            arr.put(obj3);

            JSONObject obj4 = new JSONObject();
            obj4.put("userId", "rulurala");
            obj4.put("image", R.drawable.ic_favorite_border);
            arr.put(obj4);
        }catch (JSONException e){
            e.printStackTrace();
        }

        switch (status){
            // 로그인한 사용자의 리스트만 전달
            case 1:
                try{
                    // 팔로워 리스트에 있는 사용자를 팔로우 했는지 체크
                    JSONArray following_list = followingObj.getJSONArray("data");
                    for(int i = 0; i < arr.length(); i++){
                        for(int j = 0; j < following_list.length(); j++){
                            int check = 0;
                            String following_id = following_list.getJSONObject(j).getString("userId");
                            String follower_id = arr.getJSONObject(i).getString("userId");

                            // Following
                            if(following_id == follower_id) {
                                arr.getJSONObject(i).put("flag", 1);
                                check = 1;
                                break;
                            }

                            // Unfollowing
                            if (check == 0)
                                arr.getJSONObject(i).put("flag", 0);
                        }
                    }
                    item.put("data", arr);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                break;

            // 로그인한 사용자가 아닐경우 (로그인한 사용자의 리스트 + 선택한 사용자의 리스트 전달)
            default:
                try{
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

                    // 선택한 사용자의 팔로워 리스트에 있는 사용자를 팔로우 했는지 체크
                    JSONArray following_list = followingObj.getJSONArray("data");
                    for(int i = 0; i < user_arr.length(); i++){
                        for(int j = 0; j < following_list.length(); j++){
                            int check = 0;
                            String user_follower_id = user_arr.getJSONObject(i).getString("userId");
                            String following_id = following_list.getJSONObject(j).getString("userId");

                            // Following
                            if(user_follower_id == following_id) {
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
        }
        return item;
    }
}