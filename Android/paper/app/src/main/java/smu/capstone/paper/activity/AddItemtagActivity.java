package smu.capstone.paper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.HomeMarketAdapter;
import smu.capstone.paper.adapter.ItemSearchAdapter;

public class AddItemtagActivity extends Activity {
    SearchView searchView;
    ItemSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_itemtag);

        Intent intent = getIntent();
        searchView = findViewById(R.id.itemtag_search);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.itemtag_list);


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { // 검색어 입력 시 발생
                return false;
            }
        });*/

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        JSONObject obj = GetItemData();

        try {
            adapter = new ItemSearchAdapter(this, obj);
        } catch (JSONException e){
            e.printStackTrace();
        }
        recyclerView.setAdapter(adapter);


        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int)(display.getWidth()* 0.9);
        int height = (int)(display.getHeight() * 0.9);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }

    // server에서 data전달
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JSONObject GetItemData(){
        JSONObject item = new JSONObject();
        JSONArray arr= new JSONArray();

        //임시 데이터
        try{
            JSONObject obj1 = new JSONObject();
            obj1.put("tag_name", "A4 box");
            obj1.put("low_price", 1000);
            obj1.put("high_price", 5000);
            obj1.put("tag_img", drawable2Bitmap( getDrawable(R.drawable.ic_baseline_face_24)) );
            arr.put(obj1);

            JSONObject obj2 = new JSONObject();

            obj2.put("tag_img", drawable2Bitmap( getDrawable(R.drawable.ic_baseline_face_24)) );
            obj2.put("tag_name", "ball pen");
            obj2.put("low_price", 1000);
            obj2.put("high_price", 10000);
            arr.put(obj2);

            item.put("data", arr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return item;
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
