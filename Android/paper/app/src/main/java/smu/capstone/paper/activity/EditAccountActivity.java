package smu.capstone.paper.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.fragment.FragEditId;
import smu.capstone.paper.fragment.FragEditPw;
import smu.capstone.paper.fragment.FragFindId;
import smu.capstone.paper.fragment.FragFindPw;

public class EditAccountActivity extends AppCompatActivity {
    //private EditText account_newid, account_pw, account_newpw, account_newpw_check;
    private Button edit_id_btn, edit_pw_btn, edit_account_btn;


    private final int Frag_editid = 1;
    private final int Frag_editpw = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_account_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("계정 관리");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24); //뒤로가기 버튼 이미지 지정

        edit_id_btn = findViewById(R.id.edit_id_btn);
        edit_pw_btn = findViewById(R.id.edit_pw_btn);
        edit_account_btn = findViewById(R.id.edit_account_btn);

        FragmentView(Frag_editid);

        edit_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(Frag_editid);
            }
        });

        edit_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(Frag_editpw);
            }
        });

        // 확인 버튼
        edit_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void FragmentView(int fragment){

        //FragmentTransactiom를 이용해 프래그먼트를 사용
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment){
            case 1:
                // 아이디 프래그먼트 호출
                FragEditId fragEditId = new FragEditId();
                transaction.replace(R.id.edit_account_frame, fragEditId);
                transaction.commit();
                break;

            case 2:
                // 비밀번호 프래그먼트 호출
                FragEditPw fragEditPw = new FragEditPw();
                transaction.replace(R.id.edit_account_frame, fragEditPw);
                transaction.commit();
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                finish();
                break;
        }
        return true;
    }

}
