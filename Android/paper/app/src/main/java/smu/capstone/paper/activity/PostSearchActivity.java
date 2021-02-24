package smu.capstone.paper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import smu.capstone.paper.R;
import smu.capstone.paper.fragment.FragPostAccount;
import smu.capstone.paper.fragment.FragPostTag;

public class PostSearchActivity extends AppCompatActivity {
    SearchView searchView;
    Button p_search_tag, p_search_id;
    ImageButton post_search_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search);

        searchView = findViewById(R.id.p_searchview);
        p_search_tag = (Button)findViewById(R.id.p_search_tag);
        p_search_id = (Button)findViewById(R.id.p_search_id);
        post_search_back = (ImageButton)findViewById(R.id.post_search_back);

        // search view 전체 영역 터치 가능
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        post_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        p_search_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragPostTag fragPostTag = new FragPostTag();
                transaction.replace(R.id.post_search_frame, fragPostTag);
                transaction.commit();
            }
        });

        p_search_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}