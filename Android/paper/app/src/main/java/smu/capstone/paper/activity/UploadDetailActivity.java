package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.SettingSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.AddItemTagAdapter;
import smu.capstone.paper.responseData.CodeResponse;
import smu.capstone.paper.responseData.ItemTag;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class UploadDetailActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    String filePath, login_id;
    RecyclerView itemtag_rv;
    AddItemTagAdapter adapter;
    List<ItemTag> itemTagData = new ArrayList<>();
    EditText hashtagText, uploadText;
    Switch upload_detail_ccl_1, upload_detail_ccl_2, upload_detail_ccl_3 , upload_detail_ccl_4, upload_detail_ccl_5;
    ImageView upload_detail_img;
    int ccl1, ccl2, ccl3, ccl4, ccl5;

    RequestBody requestFile, requestId, requestText;
    MultipartBody.Part body;
    ArrayList<MultipartBody.Part> hash_tags = new ArrayList<>();
    ArrayList<MultipartBody.Part> ccl_list = new ArrayList<>();
    ArrayList<MultipartBody.Part> item_tags = new ArrayList<>();

    final int ON = 1;
    final int OFF = 0;

    long first_time = 0;
    long second_time = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_detail);

        uploadText = findViewById(R.id.upload_text);
        login_id = LoginSharedPreference.getLoginId(this);


        //이미지 세팅
        filePath = getIntent().getStringExtra("path");
        upload_detail_img = findViewById(R.id.upload_detail_pic);
        Glide.with(this).load(filePath).into(upload_detail_img);

        //ccl 세팅
        upload_detail_ccl_1 = findViewById(R.id.upload_detail_ccl_1);
        upload_detail_ccl_2 = findViewById(R.id.upload_detail_ccl_2);
        upload_detail_ccl_3 = findViewById(R.id.upload_detail_ccl_3);
        upload_detail_ccl_4 = findViewById(R.id.upload_detail_ccl_4);
        upload_detail_ccl_5 = findViewById(R.id.upload_detail_ccl_5);

        upload_detail_ccl_1.setChecked(SettingSharedPreference.getSetting(this,"ccl1"));
        upload_detail_ccl_2.setChecked(SettingSharedPreference.getSetting(this,"ccl2"));
        upload_detail_ccl_3.setChecked(SettingSharedPreference.getSetting(this,"ccl3"));
        upload_detail_ccl_4.setChecked(SettingSharedPreference.getSetting(this,"ccl4"));
        upload_detail_ccl_5.setChecked(SettingSharedPreference.getSetting(this,"ccl5"));

        // 툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.upload_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        //해쉬태그 작성
        hashtagText = findViewById(R.id.upload_hashtag);
        hashtagText.append("#");
        hashtagText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if(source.charAt(i) == ' ')
                        return " #";
                    else if(source.charAt(i) == '#')
                        continue;
                    else if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        }});

        hashtagText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG", charSequence.length() + ":" + charSequence+"");
                if(charSequence.length()==0)
                    hashtagText.append("#");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        // 아이템 태그 어뎁터 설정
        itemtag_rv = findViewById(R.id.upload_itemtag_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        itemtag_rv.setLayoutManager(layoutManager);
        itemTagData.add(new ItemTag(-1));
        adapter = new AddItemTagAdapter(itemtag_rv.getContext(), itemTagData); // 추가모드 어뎁터 세팅

        // 적용
        itemtag_rv.setAdapter(adapter);






    }

    // 서버에 전송할 데이터 묶기
    public void makeUploadData(){
        hash_tags.clear();
        ccl_list.clear();
        ccl1 = ccl2 = ccl3 = ccl4 = ccl5 = OFF;

        // 업로드 이미지
        File file = new File(filePath);
        requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        body = MultipartBody.Part.createFormData("img_post", file.getName(), requestFile);

        // 사용자 아이디
        requestId = RequestBody.create(MediaType.parse("text/plain"), login_id);

        // 텍스트
        String text = uploadText.getText().toString();
        requestText = RequestBody.create(MediaType.parse("text/plain"), text);

        // 해쉬태그
        String hashTag = hashtagText.getText().toString() + " ";
        Pattern pattern = Pattern.compile("[#](.*?)[ ]");
        Matcher matcher = pattern.matcher(hashTag);
        while(matcher.find()){
            hash_tags.add(MultipartBody.Part.createFormData("hash_tag", matcher.group(1)));

            if(matcher.group(1) == null)
                break;
        }

        // ccl
        if(upload_detail_ccl_1.isChecked())
            ccl1 = ON;
        if(upload_detail_ccl_2.isChecked())
            ccl2 = ON;
        if(upload_detail_ccl_3.isChecked())
            ccl3 = ON;
        if(upload_detail_ccl_4.isChecked())
            ccl4 = ON;
        if(upload_detail_ccl_5.isChecked())
            ccl5 = ON;

        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl1)));
        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl2)));
        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl3)));
        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl4)));
        ccl_list.add(MultipartBody.Part.createFormData("ccl", String.valueOf(ccl5)));

        // 아이템 태그
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JSONObject getItemtagData(){
        JSONObject post_itemtag_item = new JSONObject();
        JSONArray arr= new JSONArray();


        //임시 데이터 저장
        try{

            JSONObject obj2 = new JSONObject();
            obj2.put("Image", drawable2Bitmap( getDrawable(R.drawable.ic_baseline_add_24)) );
            arr.put(obj2);


            for(int i = 0; i < 5; i++){
                JSONObject obj = new JSONObject();
                obj.put("Image", drawable2Bitmap( getDrawable(R.drawable.sampleimg)) );
                arr.put(obj);
            }
            post_itemtag_item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return post_itemtag_item;
    }


    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if(second_time-first_time <2000){
            super.onBackPressed();
            finish();
        }
        else{
            Toast.makeText(this,"한번 더 누르면 업로드를 종료합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
               // 알림 팝업
                return true;

            case R.id.toolbar_done :
                makeUploadData();
                serviceApi.PostUpload(requestId, requestText, hash_tags, ccl_list, body).enqueue(new Callback<CodeResponse>() {
                    @Override
                    public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                        try {
                            CodeResponse result = response.body();
                            int resultCode = result.getCode();

                            if (resultCode == StatusCode.RESULT_OK) {
                                Toast toast = Toast.makeText(UploadDetailActivity.this, "업로드 완료", Toast.LENGTH_SHORT);
                                toast.show();
                                //Intent intent = new Intent(UploadDetailActivity.this, PostActivity.class); // 업데이트 된 게시물로 다시 이동
                                //startActivity(intent);
                            } else if (resultCode == StatusCode.RESULT_SERVER_ERR) {
                                new AlertDialog.Builder(UploadDetailActivity.this)
                                        .setMessage("업로드에 실패했습니다." + "\n" + "다시 시도해주세요..")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                            }
                        } catch (NullPointerException e){
                            new AlertDialog.Builder(UploadDetailActivity.this)
                                    .setMessage("에러발생!")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CodeResponse> call, Throwable t) {
                        Toast.makeText(UploadDetailActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        Log.e("게시글 업로드 에러", t.getMessage());
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
                return true;

        }
        return super.onOptionsItemSelected(item);
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