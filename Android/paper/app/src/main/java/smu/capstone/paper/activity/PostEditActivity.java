package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.AddItemTagAdapter;
import smu.capstone.paper.adapter.HashTagAdapter;
import smu.capstone.paper.adapter.ItemTagAdapter;
import smu.capstone.paper.adapter.PostCmtAdapter;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class PostEditActivity extends AppCompatActivity {

    RecyclerView itemtag_rv;
    AddItemTagAdapter adapter;
    EditText hashtagText, post_edit_text;
    ImageView post_edit_pic;
    Switch post_edit_ccl_1, post_edit_ccl_2, post_edit_ccl_3, post_edit_ccl_4, post_edit_ccl_5;
    int ccl_cc,ccl_a,ccl_nc,ccl_nd,ccl_sa;

    int post_id, user_id;
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;
    JsonObject data, postData, userData;
    JsonArray hashTagsData,  itemTagData, CommentsData;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);

        post_edit_pic = findViewById(R.id.post_edit_pic);
        post_edit_text = findViewById(R.id.post_edit_text);
        post_edit_ccl_1 = findViewById(R.id.post_edit_ccl_1);
        post_edit_ccl_2 = findViewById(R.id.post_edit_ccl_2);
        post_edit_ccl_3 = findViewById(R.id.post_edit_ccl_3);
        post_edit_ccl_4 = findViewById(R.id.post_edit_ccl_4);
        post_edit_ccl_5 = findViewById(R.id.post_edit_ccl_5);

        hashtagText = findViewById(R.id.post_edit_hashtag);
        itemtag_rv = findViewById(R.id.post_edit_itemtag_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        itemtag_rv.setLayoutManager(layoutManager);


        Toolbar toolbar = (Toolbar) findViewById(R.id.post_edit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("게시글 수정");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        // PostActivity에서 전달받은 기존 내용들로 셋팅
        Intent intent = getIntent();
        post_id = intent.getIntExtra("postID", -1);
        user_id = LoginSharedPreference.getUserId(PostEditActivity.this);


        getData();






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
                finish();
                break;

            case R.id.toolbar_done : // 확인 버튼 눌렀을 때

                Toast toast = Toast.makeText(PostEditActivity.this, "수정완료", Toast.LENGTH_SHORT);
                toast.show();

                //서버에 변경내용 전송

                Intent intent = new Intent(PostEditActivity.this, PostActivity.class); // 업데이트 된 게시물로 다시 이동
                intent.putExtra("post_id", post_id);
                startActivity(intent);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }



    public void getData(){
        serviceApi.GetDetailPost(post_id, user_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();

                int resultCode = result.get("code").getAsInt();

                if( resultCode == statusCode.RESULT_OK){
                    data = result.getAsJsonObject("data");
                    setPostData();
                    setHashTagData();
                    setItemTagData();

                }
                else if(resultCode == statusCode.RESULT_SERVER_ERR){
                    Toast.makeText(PostEditActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    // 빈화면 말고 다른행동..

                }
                else {
                    Toast.makeText(PostEditActivity.this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PostEditActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });


    }

    public boolean setPostData(){
        postData = data.getAsJsonObject("post");
        userData = data.getAsJsonObject("user");


        // 게시글 정보 세팅
        post_edit_text.setText(postData.get("text").getAsString());
        Glide.with(this).load(RetrofitClient.getBaseUrl() + postData.get("image").getAsString()).into(post_edit_pic);


        //CCL 세팅
        ccl_cc =postData.get("ccl").getAsJsonObject().get("ccl_cc").getAsInt();
        ccl_a =postData.get("ccl").getAsJsonObject().get("ccl_a").getAsInt();
        ccl_nc =postData.get("ccl").getAsJsonObject().get("ccl_nc").getAsInt();
        ccl_nd =postData.get("ccl").getAsJsonObject().get("ccl_nd").getAsInt();
        ccl_sa =postData.get("ccl").getAsJsonObject().get("ccl_sa").getAsInt();

        post_edit_ccl_1.setChecked(ccl_cc==1);
        post_edit_ccl_2.setChecked(ccl_a==1);
        post_edit_ccl_3.setChecked(ccl_nc==1);
        post_edit_ccl_4.setChecked(ccl_nd==1);
        post_edit_ccl_5.setChecked(ccl_sa==1);

        return true;
    }
    public boolean setHashTagData(){

        hashTagsData = data.getAsJsonArray("hashTags");
        //해쉬 태그 어뎁터 설정

        String hashtags = "";
        for(int i = 0; i < hashTagsData.size(); i++){
            JsonObject item = hashTagsData.get(i).getAsJsonObject();
            String text = "#"+item.get("text").getAsString() +" ";
            hashtags += text;
        }
        hashtags +="#";
        hashtagText.setText(hashtags);
        //해쉬태그 작성

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
                if(charSequence.length()==0)
                    hashtagText.append("#");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return true;
    }
    public boolean setItemTagData(){
        itemTagData = data.getAsJsonArray("itemTags");
        JsonObject addElement = new JsonObject();
        addElement.addProperty("id" , -1);
        itemTagData.add(addElement);
        adapter = new AddItemTagAdapter(itemtag_rv.getContext(), itemTagData); // 추가모드 어뎁터 세팅
        itemtag_rv.setAdapter(adapter);

        return true;
    }

}