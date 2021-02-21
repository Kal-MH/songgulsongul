package smu.capstone.paper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import smu.capstone.paper.data.FeedData;
import smu.capstone.paper.data.ProfileData;

public class FragHomeProfile extends Fragment {
    ImageButton button;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_profile, container, false);

        button = (ImageButton)view.findViewById(R.id.profile_setting_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(),v);
                popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item){
                        switch(item.getItemId()){
                            case R.id.profile_edit:
                                Toast.makeText(getContext(),"프로필 편집",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.profile_keep:
                                Toast.makeText(getContext(),"보관함",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.profile_logout:
                                Toast.makeText(getContext(),"로그아웃",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.profile_setting:
                                Toast.makeText(getContext(),"설정",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        // view에서 id 찾아야함
        GridView gridView = view.findViewById(R.id.profile_grid);

        //아이템 추가
        int[] i = {R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg,
                R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg,
                R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg,
                R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg,
                R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg
        };

        ProfileData data = new ProfileData("wonhee",drawable2Bitmap(getResources().getDrawable(R.drawable.sampleimg)),
                20,204,
                204,"hello", "twitter.com",4000);

        // 어뎁터 적용
        PostImageAdapter adapter = new PostImageAdapter(this.getContext(), R.layout.post_image_item, i);
        gridView.setAdapter(adapter);

        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Log.d("TAG", position + "is Clicked");      // Can not getting this method.

            }
        });

        return view;
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

