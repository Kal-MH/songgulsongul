package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.AddItemTagAdapter;

public class PostEditActivity extends AppCompatActivity {

    RecyclerView itemtag_rv;
    AddItemTagAdapter adapter;
    EditText hashtagText, post_edit_text;
    ImageView post_edit_pic;
    JsonObject itemtag_obj, hashtag_obj;
    JsonArray hashtag_list;
    Switch post_edit_ccl_1, post_edit_ccl_2, post_edit_ccl_3, post_edit_ccl_4, post_edit_ccl_5;
    boolean ccl1,ccl2,ccl3,ccl4,ccl5;
    //int ccl[] = {1,0,1,1,1}; // 넘겨받은 ccl설정 값 --> 추후 intent로 PostActivity에서 넘겨받음

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.post_edit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("게시글 수정");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        // PostActivity에서 전달받은 기존 내용들로 셋팅
        Intent intent = getIntent();
        Glide.with(PostEditActivity.this).load(intent.getIntExtra("postImg", 0)).into(post_edit_pic);
        post_edit_text.setText(intent.getStringExtra("text"));
        ccl1=intent.getBooleanExtra("ccl1",false);
        ccl2=intent.getBooleanExtra("ccl2",false);
        ccl3=intent.getBooleanExtra("ccl3",false);
        ccl4=intent.getBooleanExtra("ccl4",false);
        ccl5=intent.getBooleanExtra("ccl5",false);


        // 기존 ccl 설정값에 따라 셋팅
        post_edit_ccl_1.setChecked(ccl1);
        post_edit_ccl_2.setChecked(ccl2);
        post_edit_ccl_3.setChecked(ccl3);
        post_edit_ccl_4.setChecked(ccl4);
        post_edit_ccl_5.setChecked(ccl5);


      /*  hashtag_obj =  intent.??
        hashtag_list = hashtag_obj.getAsJsonArray("data");
        hashtagText = findViewById(R.id.post_edit_hashtag);
        for(int i = 0; i < hashtag_list.size(); i++){
            hashtagText.append(hashtag_list.get(i).getAsJsonObject().get("text").getAsString());
            if(i < hashtag_list.size() - 1)
                hashtagText.append(" ");
        }
        */

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
                Log.d("TAG", charSequence.length() + ":" + charSequence+"");
                if(charSequence.length()==0)
                    hashtagText.append("#");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


/*
        itemtag_obj = getItemtagData();

        // 아이템 태그 어뎁터 설정
        itemtag_rv = findViewById(R.id.post_edit_itemtag_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        itemtag_rv.setLayoutManager(layoutManager);
        adapter = new AddItemTagAdapter(itemtag_rv.getContext(), itemtag_obj); // 추가모드 어뎁터 세팅

        // 적용
        itemtag_rv.setAdapter(adapter);

*/


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JSONObject getItemtagData(){
        JSONObject post_itemtag_item = new JSONObject();
        JSONArray arr= new JSONArray();
        //임시 데이터 저장
        try{


            JSONObject obj2 = new JSONObject();
         //   obj2.put("Image", drawable2Bitmap( getDrawable(R.drawable.ic_baseline_add_24)) );
            arr.put(obj2);

            for(int i = 0; i < 5; i++){
                JSONObject obj = new JSONObject();
         //       obj.put("Image", drawable2Bitmap( getDrawable(R.drawable.sampleimg)) );
                arr.put(obj);
            }
            post_itemtag_item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return post_itemtag_item;
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
                // 서버에 변경된 data(postEdit객체) 전송

                //if resultCode == 200
                Toast toast = Toast.makeText(PostEditActivity.this, "수정완료", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(PostEditActivity.this, PostActivity.class); // 업데이트 된 게시물로 다시 이동
                startActivity(intent);

                //if resultCode == 500
                /*Toast toast = Toast.makeText(PostEditActivity.this, "수정실패", Toast.LENGTH_SHORT);
                toast.show();*/
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}