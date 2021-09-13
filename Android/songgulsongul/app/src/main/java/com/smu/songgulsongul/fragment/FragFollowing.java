package com.smu.songgulsongul.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.EditAccountActivity;
import com.smu.songgulsongul.adapter.FollowAdapter;
import com.smu.songgulsongul.data.FollowListData;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class FragFollowing extends Fragment {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;

    RecyclerView rv;
    FollowAdapter adapter;

    int status;
    final int MY = 1;
    final int OTHER = 0;

    String login_id;
    String user_id;
    JsonObject login_following_list = new JsonObject();
    JsonObject user_following_list = new JsonObject();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_following, container, false);

        rv = rootView.findViewById(R.id.follow_following_rv);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        login_id = LoginSharedPreference.getLoginId(getContext());

        Bundle bundle = getArguments();
        if(!login_id.equals(bundle.getString("userId"))) {
            user_id = bundle.getString("userId");
            status = OTHER;
        }
        else {
            status = MY;
        }

        if(status == MY)
            getFollowingData();
        else
            getUserFollowingData();

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
    public void getFollowingData(){
        FollowListData data = new FollowListData(login_id);
        serviceApi.LFollowList(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();
                int resultCode = result.get("code").getAsInt();

                if(resultCode == statusCode.RESULT_OK){
                    login_following_list.add("data", result.getAsJsonArray("followinfo"));
                    if(status == MY) {
                        adapter = new FollowAdapter(getContext(), login_following_list, status);
                        rv.setAdapter(adapter);
                    }
                }
                else if(resultCode == statusCode.RESULT_CLIENT_ERR){

                    View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                    Context context = getActivity();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ImageView icon=dialogView.findViewById(R.id.warning);

                    TextView txt=dialogView.findViewById(R.id.txtText);
                    txt.setText("에러가 발생했습니다."+"\n"+"다시 시도해주세요.");

                    Button ok_btn = dialogView.findViewById(R.id.okBtn);
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    Button cancel_btn = dialogView.findViewById(R.id.cancelBtn);
                    cancel_btn.setVisibility(View.GONE);
                }
                else{
                    Toasty.normal(getContext(), "서버와의 통신이 불안정합니다.").show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toasty.normal(getContext(), "서버와의 통신이 불안정합니다.").show();
                Log.e("팔로우 리스트 불러오기 에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
    }

    // server에서 data전달 --> 로그인한 사용자가 아닐경우 (로그인한 사용자의 리스트 + 선택한 사용자의 리스트 전달)
    public void getUserFollowingData(){
        FollowListData data = new FollowListData(login_id);
        data.addUserId(user_id);
        serviceApi.FollowList(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();
                int resultCode = result.get("code").getAsInt();

                if(resultCode == statusCode.RESULT_OK){
                    JsonArray login_list = result.getAsJsonArray("loginFollowInfo");
                    JsonArray user_list = result.getAsJsonArray("userFollowInfo");
                    // 선택한 사용자의 팔로우 리스트에 있는 사용자를 팔로우 했는지 체크
                    for(int i = 0; i < user_list.size(); i++){
                        if(login_list.size() == 0) {
                            user_list.get(i).getAsJsonObject().addProperty("flag", false);
                            continue;
                        }
                        for(int j = 0; j < login_list.size(); j++){
                            int check = 0;
                            String user_follower_id = user_list.get(i).getAsJsonObject().get("userId").getAsString();
                            String follower_id = login_list.get(j).getAsJsonObject().get("userId").getAsString();

                            // Following
                            if(user_follower_id.equals(follower_id)) {
                                user_list.get(i).getAsJsonObject().addProperty("flag", true);
                                check = 1;
                                break;
                            }

                            // Unfollowing
                            if (check == 0)
                                user_list.get(i).getAsJsonObject().addProperty("flag", false);
                        }
                    }
                    user_following_list.add("data", user_list);
                    adapter = new FollowAdapter(getContext(), user_following_list, status);
                    rv.setAdapter(adapter);

                }
                else if(resultCode == statusCode.RESULT_CLIENT_ERR){

                    View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                    Context context = getActivity();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ImageView icon=dialogView.findViewById(R.id.warning);

                    TextView txt=dialogView.findViewById(R.id.txtText);
                    txt.setText("에러가 발생했습니다."+"\n"+"다시 시도해주세요.");

                    Button ok_btn = dialogView.findViewById(R.id.okBtn);
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
                else{
                    Toasty.normal(getContext(), "서버와의 통신이 불안정합니다.").show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toasty.normal(getContext(), "서버와의 통신이 불안정합니다.").show();
                Log.e("사용자 팔로우 리스트 불러오기 에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
    }

}