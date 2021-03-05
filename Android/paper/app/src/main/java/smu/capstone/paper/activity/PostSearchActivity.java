package smu.capstone.paper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import smu.capstone.paper.R;
import smu.capstone.paper.fragment.FragPostAccount;
import smu.capstone.paper.fragment.FragPostTag;

public class PostSearchActivity extends AppCompatActivity {
    SearchView searchView;
    Button p_search_tag, p_search_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search);

        searchView = findViewById(R.id.p_searchview);
        p_search_tag = (Button)findViewById(R.id.p_search_tag);
        p_search_id = (Button)findViewById(R.id.p_search_id);


        Toolbar toolbar = (Toolbar) findViewById(R.id.post_search_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayShowTitleEnabled(false); // 타이틀 지우고싶을경우
        actionBar.setTitle("Search");

        //아래는 좌측에 뒤로가기버튼 만들고싶을경우
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        SpannableString content = new SpannableString("태그");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0); p_search_tag.setText(content);



        // search view 전체 영역 터치 가능
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 서버에 query객체 전달 코드 작성
                // ----------------------------

                // if resultCode == 200
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        p_search_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableString content = new SpannableString("태그");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0); p_search_tag.setText(content);
                p_search_id.setText("계정");

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragPostTag fragPostTag = new FragPostTag();
                transaction.replace(R.id.post_search_frame, fragPostTag);
                transaction.commit();
            }
        });

        p_search_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableString content = new SpannableString("계정");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0); p_search_id.setText(content);
                p_search_tag.setText("태그");

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragPostAccount fragPostAccount = new FragPostAccount();
                transaction.replace(R.id.post_search_frame, fragPostAccount);
                transaction.commit();
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragPostTag fragPostTag = new FragPostTag();
        transaction.replace(R.id.post_search_frame, fragPostTag);
        transaction.commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}