package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
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

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.data.KeepData;
import smu.capstone.paper.data.UserData;
import smu.capstone.paper.responseData.Post;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class KeepActivity extends AppCompatActivity {
    // ServiceApi 객체 생성
    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    PostImageAdapter adapter;
    GridView gridView;
    TextView keep_count, keep_id;
    ImageView keep_imae;
    JsonObject keep_data;
    String login_id;

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

        login_id = LoginSharedPreference.getLoginId(KeepActivity.this);

        // view에서 id 찾아야함
        gridView = findViewById(R.id.keep_grid);

        getKeepData();

        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(KeepActivity.this, PostActivity.class);

                // 게시글 id 전달
                int postId = keep_data.getAsJsonArray("keepInfo").get(position).getAsJsonObject().get("postId").getAsInt();
                intent.putExtra("post_id", postId);

                startActivity(intent);

                Log.d("TAG", position + "is Clicked");      // Can not getting this method.
            }
        });

    }

    public void setKeepData(JsonObject data){
        List<Post> keep_info = null;
        JsonArray keep_info_arr = data.getAsJsonArray("keepInfo");
        for( JsonElement i :keep_info_arr){
            keep_info.add(new Post(i.getAsJsonObject().get("id").getAsInt() , i.getAsJsonObject().get("image").getAsString()));
        }


        // 로그인한 Id로 셋팅
        keep_id.setText(login_id);

        // 프로필 이미지 셋팅
        String profile_image = RetrofitClient.getBaseUrl() + data.get("profileImg").getAsString();
        Glide.with(this).load(profile_image).into(keep_imae);

        // 보관한 게시글 개수 셋팅
        int keep_cnt = data.get("keepCnt").getAsInt();
        keep_count.setText("보관한 게시글 " + keep_cnt);

        // 어뎁터 적용
        adapter = new PostImageAdapter(this, R.layout.post_image_item, keep_info);
        gridView.setAdapter(adapter);
    }

    //server에서 data전달
    public void getKeepData(){
        KeepData data = new KeepData(login_id);
        serviceApi.Keep(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();
                int resultCode = result.get("code").getAsInt();

                if(resultCode == StatusCode.RESULT_OK){
                    keep_data = result;
                    setKeepData(result);
                }
                else if(resultCode == StatusCode.RESULT_SERVER_ERR){
                    new AlertDialog.Builder(KeepActivity.this)
                            .setTitle("경고")
                            .setMessage("에러가 발생했습니다."+"\n"+"다시 시도해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 에러 발생 시 새로고침
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
                else{
                    Toast.makeText(KeepActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(KeepActivity.this, "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                Log.e("보관함 데이터 불러오기 에러", t.getMessage());
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

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