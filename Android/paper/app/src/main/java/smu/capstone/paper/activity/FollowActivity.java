package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import smu.capstone.paper.R;
import smu.capstone.paper.fragment.FragFollower;
import smu.capstone.paper.fragment.FragFollowing;

public class FollowActivity extends AppCompatActivity {
    private FragmentManager fm;
    private FragmentTransaction ft;

    private TextView follow_follow;
    private TextView follow_follwer;

    private FragFollowing fragFollowing;
    private FragFollower fragFollower;

    private ImageButton follow_back;
    private ImageView follow_img;
    private TextView follow_id;
    private TextView follow_intro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);




        follow_intro = findViewById(R.id.follow_intro);
        follow_img = findViewById(R.id.follow_image);
        follow_id = findViewById(R.id.follow_id);
        follow_follow = findViewById(R.id.follow_follow); // 내가 팔로잉 하는사람
        follow_follwer = findViewById(R.id.follow_follower); // 나를 팔로잉 하는사람들
                                            //나의 팔로워
        follow_back = findViewById(R.id.follow_back);


        fragFollower = new FragFollower();
        fragFollowing = new FragFollowing();


        // 사용자 정보 초기화
        follow_id.setText("user1234");
        follow_img.setImageBitmap(drawable2Bitmap(getResources().getDrawable(R.drawable.test)));
        follow_intro.setText("안녕하세요!\n 반갑습니다~!");

        //팔로우에 밑줄 긋고 팔로우창띄움
        SpannableString content = new SpannableString("팔로우");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0); follow_follow.setText(content);

        fm = getSupportFragmentManager();

        ft = fm.beginTransaction();
        ft.replace(R.id.follow_frag, fragFollower);
        ft.commit();


        follow_follow.setOnClickListener(new View.OnClickListener() { // 나를 팔로잉 하는 사람들 보여줌
            @Override
            public void onClick(View view) {

                //팔로우에 밑줄 긋고 팔로우창띄움
                SpannableString content = new SpannableString("팔로우");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0); follow_follow.setText(content);
                follow_follwer.setText("팔로잉");

                ft = fm.beginTransaction();
                ft.replace(R.id.follow_frag, fragFollower);
                ft.commit();
            }
        });

        follow_follwer.setOnClickListener(new View.OnClickListener() { // 내가 팔로잉 하는사람들 보여줌
            @Override
            public void onClick(View view) {

                //팔로우에 밑줄 긋고 팔로우창띄움
                follow_follow.setText("팔로우");
                SpannableString content = new SpannableString("팔로잉");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0); follow_follwer.setText(content);

                ft = fm.beginTransaction();
                ft.replace(R.id.follow_frag, fragFollowing);
                ft.commit();
            }
        });

        follow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


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