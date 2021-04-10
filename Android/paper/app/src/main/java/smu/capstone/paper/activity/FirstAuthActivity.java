package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        if(LoginSharedPreference.getUserId(FirstAuthActivity.this).length() == 0) {
            // call Login Activity
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            // Call Next Activity
            intent = new Intent(FirstAuthActivity.this, HomeActivity.class);
            intent.putExtra("STD_NUM", LoginSharedPreference.getUserId(this).toString());
            startActivity(intent);
            this.finish();
        }
    }
}