package com.smu.songgulsongul.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.SettingSharedPreference;
import com.smu.songgulsongul.activity.FirstAuthActivity;
import com.smu.songgulsongul.activity.PostActivity;
import com.smu.songgulsongul.data.user.TokenData;
import com.smu.songgulsongul.data.CodeResponse;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingService extends FirebaseMessagingService {


    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(!SettingSharedPreference.getSetting(this, "alert1")){ //수신거부
            return;
        }
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String mode = remoteMessage.getData().get("mode");
        String sender = remoteMessage.getData().get("sender");


        if( !SettingSharedPreference.getSetting(this, "alert"+mode ) ) //해당모드 false상태이면
            return;


        final String CHANNEL_ID = "ChannerID";
        NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "ChannerName";
            final String CHANNEL_DESCRIPTION = "ChannerDescription";
            final int importance = NotificationManager.IMPORTANCE_HIGH;

            // add in API level 26
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(mChannel);
        }


        Intent intent;
        PendingIntent pendingIntent;
        if(mode.equals("2") || mode.equals("3")){ // 댓글, 좋아요 --> 특정 게시글로 이동

            Log.d("alaert",  Integer.parseInt(sender) +", "+ LoginSharedPreference.getUserId(this) );
            if( Integer.parseInt(sender) == LoginSharedPreference.getUserId(this) ) //셀프 좋아요, 댓글
                return;


            String postid = remoteMessage.getData().get("postid");
            int postId = Integer.parseInt(postid);
            intent = new Intent(this, PostActivity.class);
            intent.putExtra("post_id", postId);
            if(mode.equals("2")) {
                intent.putExtra("comment", true);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        }
        else { //홈 화면으로 이동

            intent = new Intent(this, FirstAuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(message);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setContentTitle(title);
            builder.setVibrate(new long[]{500, 500});
        }
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        mManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }

    void sendRegistrationToServer(String token){
        TokenData tokenData = new TokenData(LoginSharedPreference.getUserId(this), token);
        serviceApi.setToken(tokenData)
                .enqueue(new Callback<CodeResponse>() {
                    @Override
                    public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {


                    }
                    @Override
                    public void onFailure(Call<CodeResponse> call, Throwable t) {
                        Log.e("Token" , "등록 실패");
                    }
         });

    }
}