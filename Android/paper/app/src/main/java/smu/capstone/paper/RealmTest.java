package smu.capstone.paper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;
import io.realm.RealmResults;
import smu.capstone.paper.data.Sticker;

public class RealmTest extends AppCompatActivity {

    Realm realm;
    RealmResults<Sticker> realmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_test);

        realm.init(this);
        realm = Realm.getDefaultInstance();

        realmResults = realm.where(Sticker.class).findAll();

        // 임시파일 넣음
        if(realmResults.isEmpty()){
            byte[] temp = drawable2Bytes(getResources().getDrawable(R.drawable.test));

            realm.beginTransaction();
            Sticker tempItem = realm.createObject(Sticker.class,1);
            tempItem.setImage(temp);
            realm.commitTransaction();

        }

        Button btn = findViewById(R.id.temp_realm_get_btn);
        final ImageView imageView = findViewById(R.id.temp_realm_image);
        final TextView textView = findViewById(R.id.temp_realm_id);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realmResults = realm.where(Sticker.class).findAll();
                int id = realmResults.get(0).getId();
                byte[] imageArray = realmResults.get(0).getImage();
                textView.setText("ID : " + id);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageArray,0,imageArray.length);
                imageView.setImageBitmap(bitmap);
            }
        });


    }

    public static byte[] drawable2Bytes(Drawable d) {
        Bitmap bitmap = drawable2Bitmap(d);
        return bitmap2Bytes(bitmap);
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
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
