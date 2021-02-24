package smu.capstone.paper.fragment;

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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.HomeMarketAdapter;
import smu.capstone.paper.item.HomeMarketItem;

public class FragFindId extends Fragment {

    public FragFindId() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_find_id, container, false);

    }

    public static class FragHomeMarket extends Fragment {
        private View view;
        private SearchView searchView;
        ArrayList<HomeMarketItem> items = new ArrayList<HomeMarketItem>();

        public void addItem(HomeMarketItem item){
            items.add(item);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.frag_home_market, container, false);

            searchView = view.findViewById(R.id.market_search);
            GridView gridView = view.findViewById(R.id.market_grid);

            // 아이템 추가
            addItem(new HomeMarketItem(R.drawable.sampleimg, "sample1", "20p"));
            addItem(new HomeMarketItem(R.drawable.test, "sample2", "10p"));
            addItem(new HomeMarketItem(R.drawable.ic_favorite, "sample3", "50p"));
            addItem(new HomeMarketItem(R.drawable.test, "sample4", "60p"));
            addItem(new HomeMarketItem(R.drawable.test, "sample5", "80p"));
            addItem(new HomeMarketItem(R.drawable.test, "sample6", "100p"));
            addItem(new HomeMarketItem(R.drawable.test, "sample7", "200p"));
            addItem(new HomeMarketItem(R.drawable.test, "sample8", "30p"));
            addItem(new HomeMarketItem(R.drawable.test, "sample9", "25p"));

            HomeMarketAdapter adapter = new HomeMarketAdapter(this.getContext(), R.layout.market_item, items);
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
    }

    public static class FragHomeProfile extends Fragment {
        ImageButton button;
        View view;
        TextView userId;
        ImageView profile_image;
        TextView feed_counter;
        TextView follow_counter;
        TextView follower_counter;
        TextView intro;
        TextView url;
        TextView point;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.frag_home_profile, container, false);

            userId=(TextView)view.findViewById(R.id.profile_id);
            profile_image = (ImageView)view.findViewById(R.id.profile_userimage);
            feed_counter = (TextView)view.findViewById(R.id.profile_feed_cnt);
            follow_counter = (TextView)view.findViewById(R.id.profile_follow_cnt);
            follower_counter = (TextView)view.findViewById(R.id.profile_follower_cnt);
            intro=(TextView)view.findViewById(R.id.profile_intro);
            url=(TextView)view.findViewById(R.id.profile_snsurl);
            point=(TextView)view.findViewById(R.id.profile_point);

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
            userId.setText("wonhee");
            profile_image.setImageResource(R.drawable.test);
            feed_counter.setText("20");
            follow_counter.setText("204");
            follower_counter.setText("204");
            intro.setText("hello");
            url.setText("twitter.com");
            point.setText("4000p");


            int[] i = {R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg,
                    R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg,
                    R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg,
                    R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg,
                    R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg, R.drawable.sampleimg
            };

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

        public static class FragFindPw extends Fragment {

            public FragFindPw() {

            }

            @Nullable
            @Override
            public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                return inflater.inflate(R.layout.frag_find_pw, container, false);

            }
        }
    }
}
