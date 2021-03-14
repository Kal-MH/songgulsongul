package smu.capstone.paper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.CharsetEncoder;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.AddItemTagAdapter;
import smu.capstone.paper.item.ItemtagItem;

public class UploadDetailActivity extends AppCompatActivity {

    RecyclerView itemtag_rv;
    AddItemTagAdapter adapter;
    EditText hashtagText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_detail);



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
        JSONObject obj = null;
        try {
            adapter = new AddItemTagAdapter(itemtag_rv.getContext(),obj ); // 추가모드 어뎁터 세팅
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //첫 데이터는 언제나 추가 아이콘으로 세팅
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
        );

        // 적용
        itemtag_rv.setAdapter(adapter);


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

            case R.id.toolbar_done :
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