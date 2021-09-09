package com.smu.songgulsongul.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.PostActivity;
import com.smu.songgulsongul.activity.ProfileActivity;
import com.smu.songgulsongul.data.NotificationData;
import com.smu.songgulsongul.data.RequestNotification;
import com.smu.songgulsongul.responseData.CodeResponse;
import com.smu.songgulsongul.data.FollowData;
import com.smu.songgulsongul.server.DefaultImage;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;

    Context context;
    JsonObject obj = new JsonObject();
    JsonArray dataList;
    int itemCnt;
    int status;
    String login_id;

    public FollowAdapter (Context context, JsonObject obj, int status) {
        this.context = context;
        this.obj = obj;
        this.status = status;
        login_id = LoginSharedPreference.getLoginId(context);

        dataList = obj.getAsJsonArray("data");
        itemCnt = dataList.size();
    }

    // 받아온 데이터로 팔로우/팔로워 리스트 내용 셋팅
    public void setItem(@NonNull FollowAdapter.ViewHolder holder, JsonElement item){
        holder.userid.setText(item.getAsJsonObject().get("userId").getAsString());
        String pro_image = item.getAsJsonObject().get("image").getAsString();
        String img_addr;

        if(pro_image.equals(DefaultImage.DEFAULT_IMAGE))
            img_addr = RetrofitClient.getBaseUrl() + pro_image;
        else
            img_addr = pro_image;

        Glide.with(context).load(img_addr).into(holder.profile_image);
    }

    @NonNull
    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.follow_item, parent, false);
        return new FollowAdapter.ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final FollowAdapter.ViewHolder holder, final int position) {
            final JsonElement item = dataList.get(position);
            setItem(holder, item);

            holder.follow_btn.setVisibility(View.GONE);
            holder.follow_text.setVisibility(View.VISIBLE);

            if(status != 1) { // 로그인한 사용자가 아닐경우
                if (item.getAsJsonObject().get("flag").getAsBoolean() == true) {
                    holder.follow_btn.setVisibility(View.GONE);
                    holder.follow_text.setVisibility(View.VISIBLE);
                } else if (item.getAsJsonObject().get("flag").getAsBoolean() == false) {
                    if(item.getAsJsonObject().get("userId").getAsString().equals(login_id)){
                        holder.follow_btn.setVisibility(View.GONE);
                        holder.follow_text.setVisibility(View.GONE);
                    }
                    else{
                        holder.follow_btn.setVisibility(View.VISIBLE);
                        holder.follow_text.setVisibility(View.GONE);
                    }
                }
            }

        holder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_id = item.getAsJsonObject().get("userId").getAsString();
                //팔로우 액션 --> server에 FolowData객체 전달
                FollowData data = new FollowData(login_id, user_id);
                serviceApi.Follow(data).enqueue(new Callback<CodeResponse>() {
                    @Override
                    public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                        CodeResponse result = response.body();
                        int resultCode = result.getCode();
                        if(resultCode == statusCode.RESULT_OK){
                            holder.follow_btn.setVisibility(View.GONE);
                            holder.follow_text.setVisibility(View.VISIBLE);

                            // 알림 보내기
                            NotificationData notificationData = new NotificationData(login_id+ context.getString(R.string.follow_noti), context.getString(R.string.follow_title));
                            RequestNotification requestNotification = new RequestNotification();
                            requestNotification.setSendNotificationModel(notificationData);
                            requestNotification.setMode(1);
                            requestNotification.setLoginid(user_id);
                            requestNotification.setSender( LoginSharedPreference.getUserId(context));
                            retrofit2.Call<ResponseBody> responseBodyCall = serviceApi.sendChatNotification(requestNotification);
                            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                }
                                @Override
                                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                                }
                            });
                        }
                        else if(resultCode == statusCode.RESULT_CLIENT_ERR){
                            new AlertDialog.Builder(context)
                                    .setTitle("경고")
                                    .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .show();
                        }
                        else {
                            Toasty.normal(context, "서버와의 통신이 불안정합니다").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CodeResponse> call, Throwable t) {
                        Toasty.normal(context, "서버와의 통신이 불안정합니다").show();
                        Log.e("팔로우 하기 에러", t.getMessage());
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemCnt;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_image;
        TextView userid;
        Button follow_btn;
        TextView follow_text;
        boolean following;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            profile_image=(ImageView)itemView.findViewById(R.id.follow_item_img);
            userid = (TextView)itemView.findViewById(R.id.follow_item_id);
            follow_btn = (Button)itemView.findViewById(R.id.follow_item_btn);
            follow_text = (TextView)itemView.findViewById(R.id.follow_item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    // 클릭한 사용자의 id 전달
                    JsonElement item = dataList.get(pos);
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userId", item.getAsJsonObject().get("userId").getAsString());
                        context.startActivity(intent);
                    }
                }
            });
        }



        public void setItem(Object item){

        }

    }
}
