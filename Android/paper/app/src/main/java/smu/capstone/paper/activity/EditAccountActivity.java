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

import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;

public class EditAccountActivity extends AppCompatActivity {
    private EditText account_newid, account_newpw, account_newpw_check;
    private Button account_id_check;
    private TextView edit_account_pw_correct;

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

        account_newid = (EditText)findViewById(R.id.account_newid);
        account_newpw = (EditText)findViewById(R.id.account_newpw);
        account_newpw_check = (EditText)findViewById(R.id.account_newpw_check);
        account_id_check = findViewById(R.id.account_id_check);
        edit_account_pw_correct = findViewById(R.id.edit_account_pw_correct);

        account_newid.setText(LoginSharedPreference.getLoginId(this));

        account_newpw_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.toolbar_save:
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                finish();
                break;
        }
        return true;
    }

}
