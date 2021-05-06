package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.data.UserData;
import smu.capstone.paper.item.PostItem;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;

public class KeepActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    final int RESULT_OK = 200;
    final int RESULT_CLIENT_ERR= 204;
    final int RESULT_SERVER_ERR = 500;

    PostImageAdapter adapter;
    TextView keep_count, keep_id;
    ImageView keep_imae;
    JsonObject keep_data, obj;
    int keepcnt;
    int Status = 1;
    String login_id = LoginSharedPreference.getLoginId(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep);

        keep_count = findViewById(R.id.keep_count);
        keep_id = findViewById(R.id.keep_id);
        keep_imae = findViewById(R.id.keep_imae);
        Toolbar toolbar = (Toolbar) findViewById(R.id.keep_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("보관함");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정


        // view에서 id 찾아야함
        GridView gridView = findViewById(R.id.keep_grid);

        obj = getKeepData();

        // 어뎁터 적용
        adapter = new PostImageAdapter(this, R.layout.post_image_item, obj);
        gridView.setAdapter(adapter);

        // ProfileActivity에서 전달받은 프로필 사진으로 셋팅
        //Intent intent = getIntent();
        //keep_imae.setImageResource(intent.getIntExtra("profileImg", 0));

        // 로그인한 Id로 셋팅
        keep_id.setText(login_id);

        // 보관한 게시글 개수 셋팅
        keep_count.setText("보관한 게시글 " + keepcnt);

        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(KeepActivity.this, PostActivity.class);

                // 게시글 id 전달
                int postId = obj.getAsJsonArray("keepinfo").get(position).getAsJsonObject().get("postId").getAsInt();
                intent.putExtra("postId", postId);

                startActivity(intent);

                Log.d("TAG", position + "is Clicked");      // Can not getting this method.
            }
        });

    }

    //server에서 data전달
    public JsonObject getKeepData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();

        UserData data = new UserData(login_id, Status);
        serviceApi.Keep(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();
                int resultCode = result.get("code").getAsInt();

                if(resultCode == RESULT_OK){
                    keep_data = result;
                    keepcnt = keep_data.get("keepcnt").getAsInt();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(KeepActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                Log.e("보관함 데이터 불러오기 에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

        //임시 데이터 저장
        /*try{
            JSONObject obj1 = new JSONObject();
            obj1.put("image", R.drawable.sampleimg);
            obj1.put("postId", 1);
            arr.put(obj1);

            JSONObject obj2 = new JSONObject();
            obj2.put("image", R.drawable.test);
            obj2.put("postId", 2);
            arr.put(obj2);

            JSONObject obj3 = new JSONObject();
            obj3.put("image", R.drawable.ic_baseline_emoji_emotions_24);
            obj3.put("postId", 3);
            arr.put(obj3);

            JSONObject obj4 = new JSONObject();
            obj4.put("image", R.drawable.test);
            obj4.put("postId", 4);
            arr.put(obj4);

            JSONObject obj5 = new JSONObject();
            obj5.put("image", R.drawable.sampleimg);
            obj5.put("postId", 5);
            arr.put(obj5);

            JSONObject obj6 = new JSONObject();
            obj6.put("image", R.drawable.ic_favorite);
            obj6.put("postId", 6);
            arr.put(obj6);

            JSONObject obj7 = new JSONObject();
            obj7.put("image", R.drawable.sampleimg);
            obj7.put("postId", 7);
            arr.put(obj7);

            item.put("data", arr);

            keepcnt = item.getJSONArray("data").length();
        }catch (JSONException e){
            e.printStackTrace();
        }*/
        return keep_data;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                Log.d("TAG", "뒤로,,,");
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}