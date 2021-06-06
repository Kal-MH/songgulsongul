package smu.capstone.paper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.R;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class SaveImageActivity extends Activity {
    boolean size_check;
    String post_image;
    int post_id;

    private File file, dir;
    private String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_save_image);

        final ImageView image = (ImageView)findViewById(R.id.save_img_pick);
        Button cancle_btn = (Button)findViewById(R.id.save_img_cancle);
        Button save_btn = (Button)findViewById(R.id.save_img_btn);
        Spinner save_img_size = (Spinner)findViewById(R.id.save_img_size);

        Intent intent = getIntent();
        post_id = intent.getIntExtra("post_id", -1);
        post_image = RetrofitClient.getBaseUrl() + intent.getStringExtra("post_image");
        Glide.with(SaveImageActivity.this).load(post_image).into(image);

        size_check = true;
        final int[] image_size= {400, 600, 700, 900, 1000, 1200};

        save_img_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(SaveImageActivity.this, position+"번", Toast.LENGTH_SHORT).show();
                if(position>0){
                    if(image.getWidth()<image_size[position-1]||image.getHeight()<image_size[position-1]){
                        Toast.makeText(SaveImageActivity.this, "저장된 이미지의 화질이 저하될 수 있습니다.", Toast.LENGTH_SHORT).show();
                        size_check = false;
                    }
                    else{
                        size_check=true;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!size_check) {
                    AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(SaveImageActivity.this);
                    // alert의 title과 Messege 세팅
                    myAlertBuilder.setTitle("경고");
                    myAlertBuilder.setMessage("선택한 규격이 원본의 사이즈보다 크기 때문에 이미지 저장 시 화질 저하가 될 수 있습니다. 계속 하시겠습니까?");
                    // 버튼 추가 (확인 버튼과 취소 버튼 )
                    myAlertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 확인 버튼을 눌렸을 경우
                            DownloadImage();
                            Toast.makeText(getApplicationContext(), "저장 성공!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    myAlertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 취소 버튼을 눌렸을 경우
                            Toast.makeText(getApplicationContext(), "Pressed Cancle",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
                    myAlertBuilder.show();
                } else {
                    DownloadImage();
                    Toast.makeText(SaveImageActivity.this, "저장 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int)(display.getWidth()* 0.9);
        int height = (int)(display.getHeight() * 0.9);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

    }

    public void DownloadImage(){
        fileName = post_image.substring(post_image.lastIndexOf('/') + 1, post_image.length());
        DownloadManager mgr = (DownloadManager)SaveImageActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(post_image);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("POST IMAGE")
                .setDescription("post image download")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName);
        mgr.enqueue(request);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "mnt/sdcard/Pictures" + fileName + ".jpg")));
    }
}