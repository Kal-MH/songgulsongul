package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.AddItemTagAdapter;
import smu.capstone.paper.item.ItemtagItem;

public class PostEditActivity extends AppCompatActivity {

    RecyclerView itemtag_rv;
    AddItemTagAdapter adapter;
    EditText hashtagText, post_edit_text;
    ImageView post_edit_pic;
    JSONObject itemtag_obj, hashtag_obj;
    JSONArray hashtag_list;
    Switch post_edit_ccl_1, post_edit_ccl_2, post_edit_ccl_3, post_edit_ccl_4, post_edit_ccl_5;
    int ccl[] = {1,0,1,1,1}; // 넘겨받은 ccl설정 값 --> 추후 intent로 PostActivity에서 넘겨받음

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

        // 기존 ccl 설정값에 따라 셋팅
        if(ccl[0] == 1)
            post_edit_ccl_1.setChecked(true);
        if(ccl[1] == 1)
            post_edit_ccl_2.setChecked(true);
        if(ccl[2] == 1)
            post_edit_ccl_3.setChecked(true);
        if(ccl[3] == 1)
            post_edit_ccl_4.setChecked(true);
        if(ccl[4] == 1)
            post_edit_ccl_5.setChecked(true);

        try {
            hashtag_obj = new JSONObject(intent.getStringExtra("hashtag"));
            hashtag_list = hashtag_obj.getJSONArray("data");
            hashtagText = findViewById(R.id.post_edit_hashtag);
            for(int i = 0; i < hashtag_list.length(); i++){
                hashtagText.append(hashtag_list.getJSONObject(i).getString("content"));
                if(i < hashtag_list.length() - 1)
                    hashtagText.append(" ");
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        //해쉬태그 작성
        //hashtagText = findViewById(R.id.post_edit_hashtag);
        //hashtagText.append("#");
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


        itemtag_obj = getItemtagData();

        // 기존 itemtag data로 설정 --> bitmap 형식으로 되어있어 현재 컴파일 불가
       /* try{
            itemtag_obj = new JSONObject(intent.getStringExtra("itemtag"));
            JSONObject obj = new JSONObject();
            obj.put("Image", R.drawable.ic_baseline_add_24);
            itemtag_obj.getJSONArray("data").put(0, obj);
        } catch (JSONException e){
            e.printStackTrace();
        }*/

        // 아이템 태그 어뎁터 설정
        itemtag_rv = findViewById(R.id.post_edit_itemtag_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        itemtag_rv.setLayoutManager(layoutManager);
        try {
            adapter = new AddItemTagAdapter(itemtag_rv.getContext(), itemtag_obj); // 추가모드 어뎁터 세팅
        } catch (JSONException e) {
            e.printStackTrace();
        }


/*        //첫 데이터는 언제나 추가 아이콘으로 세팅
        adapter.insertItem(
                new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_baseline_add_24)),0,"add","add" )
        );

        adapter.insertItem(
                new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" )
        );
        adapter.insertItem(
                new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" )
        );
        adapter.insertItem(
                new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" )
        );
        adapter.insertItem(
                new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" )
        );
        adapter.insertItem(
                new ItemtagItem(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_favorite)),200,"지우개","fabercastel" )
        );*/

        // 적용
        itemtag_rv.setAdapter(adapter);


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