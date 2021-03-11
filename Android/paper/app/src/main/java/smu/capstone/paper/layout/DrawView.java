package smu.capstone.paper.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import smu.capstone.paper.R;

public class DrawView extends View {

    Paint paint;
    Canvas canvas;
    ArrayList<int[]> arrayList;

    public DrawView(Context context, Rect rect, ArrayList<int[]> arr) {
        super(context);

        arrayList = arr;

        Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(),Bitmap.Config.ARGB_8888);
        //setmatrix 이용해서
        canvas = new Canvas(bitmap);



        paint = new Paint();
        paint.setColor(Color.RED);
        // 크레파스의 굵기 정하기
        paint.setStrokeWidth(10f);
        // 도화지에 좌표로 표시하기
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPoint( arrayList.get(0)[0],arrayList.get(0)[1] ,paint);
        canvas.drawPoint( arrayList.get(1)[0],arrayList.get(1)[1] ,paint);
        canvas.drawPoint( arrayList.get(2)[0],arrayList.get(2)[1] ,paint);
        canvas.drawPoint( arrayList.get(3)[0],arrayList.get(3)[1] ,paint);
    }



    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        switch (e.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                canvas.drawPoint(e.getX(), e.getY(), paint);
                Log.d("TAG", e.getX()+","+e.getY() +" ㅋㅋ");
                break;
        }
        return true;
    }


}
