package smu.capstone.paper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
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


    FragPostAccount fragPostAccount;
    FragPostTag fragPostTag;

    String keyword;

    final int POSTTAG = 123;
    final int ACCOUNT = 456;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search);

        searchView = findViewById(R.id.p_searchview);
        p_search_tag = (Button)findViewById(R.id.p_search_tag);
        p_search_id = (Button)findViewById(R.id.p_search_id);
        fragPostAccount = new FragPostAccount();
        fragPostTag = new FragPostTag();


        keyword = getIntent().getStringExtra("keyword");


        // 기본 fragment작동
        status = POSTTAG;
        setFrag(POSTTAG);


        Toolbar toolbar = (Toolbar) findViewById(R.id.post_search_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayShowTitleEnabled(false); // 타이틀 지우고싶을경우
        actionBar.setTitle("Search");
        //아래는 좌측에 뒤로가기버튼 만들고싶을경우
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정



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
                keyword = query;
                if(status == ACCOUNT){
                    fragPostAccount.getAccountData(query);
                }
                else if( status == POSTTAG){
                    fragPostTag.getTagData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // tag search button listener
        p_search_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 서버에 태그검색 요청 코드 작성
                // ----------------------------

                //if resultCode == 200
                status = POSTTAG;
                setFrag(POSTTAG);

            }
        });

        // id search button listener
        p_search_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 서버에 계정검색 요청 코드 작성
                // ---------------------------

                //if resultCode == 200
                status = ACCOUNT;
                setFrag(ACCOUNT);

            }
        });

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

    private void setFrag(int n){
        Log.d("search", "setFrag " + keyword);
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword );
        setUnderLine(n);

        switch (n) {
            case POSTTAG:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                fragPostTag.setArguments(bundle);
                transaction.replace(R.id.post_search_frame, fragPostTag);
                transaction.commit();
                break;
            case ACCOUNT:
                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                fragPostAccount.setArguments(bundle);
                transaction2.replace(R.id.post_search_frame, fragPostAccount);
                transaction2.commit();
                break;
        }

    }

    void setUnderLine(int n){
        SpannableString content;

        switch (n) {
            case POSTTAG:
                //밑줄 치기
                content = new SpannableString("태그");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0); p_search_tag.setText(content);
                p_search_id.setText("계정");


                break;
            case ACCOUNT:

                content = new SpannableString("계정");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0); p_search_id.setText(content);
                p_search_tag.setText("태그");

                break;

        }

    }
}