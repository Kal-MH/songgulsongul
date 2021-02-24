package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import smu.capstone.paper.R;

public class FollowActivity extends AppCompatActivity {

    TextView follow_follow;
    TextView follow_follwer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        follow_follow = findViewById(R.id.follow_follow);
        follow_follwer = findViewById(R.id.follow_follower);

        //팔로우에 밑줄 긋기
        SpannableString content = new SpannableString("Content");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0); follow_follow.setText(content);



    }
}