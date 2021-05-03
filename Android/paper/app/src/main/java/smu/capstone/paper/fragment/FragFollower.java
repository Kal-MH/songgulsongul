package smu.capstone.paper.fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.FollowAdapter;
import smu.capstone.paper.data.FollowListData;
import smu.capstone.paper.item.FollowItem;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;

public class FragFollower extends Fragment {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    final int RESULT_OK = 200;
    final int RESULT_CLIENT_ERR= 204;
    final int RESULT_SERVER_ERR = 500;

    RecyclerView rv;
    FollowAdapter adapter;
    FragFollowing following = new FragFollowing();
    JsonObject followingObj; // 로그인한 사용자의 following 리스트 가져오기
    JsonObject obj = new JsonObject();
    int status = 1; // 로그인한 사용자 가정
    String login_id = "test1234";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_follower, container, false);

        rv = rootView.findViewById(R.id.follow_follower_rv);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        getFollowerData();

//        adapter = new FollowAdapter(getContext(), follower_list, status);
//
//        rv.setAdapter(adapter);

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
    public void getFollowerData(){
        FollowListData data = new FollowListData(login_id);
        serviceApi.FollowerList(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();
                int resultCode = result.get("code").getAsInt();

                if(resultCode == RESULT_OK){
                    following.getFollowingData();
                    followingObj = following.login_following_list;
                    JsonArray following_list = followingObj.getAsJsonArray("data");
                    JsonArray follower_list = result.getAsJsonArray("followerInfo");

                    // 팔로워 리스트에 있는 사용자를 팔로우 했는지 체크
                    for (int i = 0; i < follower_list.size(); i++) {
                        for (int j = 0; j < following_list.size(); j++) {
                            int check = 0;
                            String following_id = following_list.get(j).getAsJsonObject().get("login_id").getAsString();
                            String follower_id = follower_list.get(i).getAsJsonObject().get("login_id").getAsString();

                            // Following
                            if (following_id == follower_id) {
                                follower_list.get(i).getAsJsonObject().addProperty("flag", 1);
                                check = 1;
                                break;
                            }

                            // Unfollowing
                            if (check == 0)
                                follower_list.get(i).getAsJsonObject().addProperty("flag", 1);
                        }
                    }
                    obj.add("data", follower_list);
                    adapter = new FollowAdapter(getContext(), obj, status);
//
//        rv.setAdapter(adapter);
                }
                else if(resultCode == RESULT_CLIENT_ERR){
                    new AlertDialog.Builder(getContext())
                            .setTitle("경고")
                            .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
                else{
                    Toast.makeText(getContext(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                Log.e("팔로워 리스트 불러오기 에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
        // 임시 데이터 저장 -- 로그인한 사용자의 팔로워 리스트
        /*try{
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
        }*/

        /*switch (status){
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
                break;*/

            // 로그인한 사용자가 아닐경우 (로그인한 사용자의 리스트 + 선택한 사용자의 리스트 전달)
           /* default:
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
        }*/
    }
}