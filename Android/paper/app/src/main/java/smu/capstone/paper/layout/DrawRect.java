package smu.capstone.paper.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.sql.Array;
import java.util.ArrayList;

public class DrawRect extends View {
    Paint paint;
    float lineThickness = 10.0f;
    ArrayList<int[]> pos;
    int gap;
    public DrawRect(Context context) {
        super(context);
        paint = new Paint();
        gap = 100;
    }

    public DrawRect(Context context, ArrayList<int[]> pos){
        super(context);
        paint = new Paint();
        this.pos = pos;
        gap = 100;
    }

    public DrawRect(Context context, ArrayList<int[]> pos, int gap){
        super(context);
        paint = new Paint();
        this.pos = pos;
        this.gap = gap;
        paint.setColor(Color.argb(255,200,0,200));
        paint.setStrokeWidth(lineThickness);
        paint.setStyle(Paint.Style.STROKE);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /* //심하게 꺾이면 삼각형이 그려지는 문제 발생. 하지만 현실적으론 종이가 이러지 않음
        int maxWidth = Math.max(Math.max(pos.get(0)[0], pos.get(1)[0]), Math.max(pos.get(2)[0],pos.get(3)[0]));
        int maxHeight = Math.max(Math.max(pos.get(0)[1], pos.get(1)[1]), Math.max(pos.get(2)[1],pos.get(3)[1]));

        int[] tl = new int[]{maxWidth,maxHeight};
        int[] tr = new int[]{maxWidth,maxHeight};
        int[] bl = new int[]{0,0};
        int[] br = new int[]{0,0};

        for (int i = 0; i < 4;i++)
        {

            int x = pos.get(i)[0];
            int y = pos.get(i)[1];

            if (x + y <= tl[0] + tl[1])
                tl = pos.get(i);
            if (y - x <= tr[1] - tr[0])
                tr = pos.get(i);
            if (y - x >= bl[1] - bl[0])
                bl = pos.get(i);
            if (x + y >= br[0] + br[1])
                br = pos.get(i);
        }

                canvas.drawLine(tl[0] +gap , tl[1] +gap ,  tr[0]+gap , tr[1] +gap , paint);
        canvas.drawLine(tr[0] +gap , tr[1]+gap  ,  br[0]+gap , br[1] +gap , paint);
        canvas.drawLine(tl[0] +gap , tl[1] +gap ,  bl[0]+gap , bl[1] +gap , paint);
        canvas.drawLine(bl[0] +gap , bl[1] +gap ,  br[0]+gap , br[1] +gap , paint);
        */


        /*
        canvas.drawLine(pos.get(0)[0] +gap , pos.get(0)[1] +gap ,  pos.get(1)[0]+gap , pos.get(1)[1] +gap , paint);
        canvas.drawLine(pos.get(1)[0] +gap , pos.get(1)[1]+gap  ,  pos.get(3)[0]+gap , pos.get(3)[1] +gap , paint);
        canvas.drawLine(pos.get(0)[0] +gap , pos.get(0)[1] +gap ,  pos.get(2)[0]+gap , pos.get(2)[1] +gap , paint);
        canvas.drawLine(pos.get(2)[0] +gap , pos.get(2)[1] +gap ,  pos.get(3)[0]+gap , pos.get(3)[1] +gap , paint);
        */

        canvas.drawLine(pos.get(0)[0] +gap , pos.get(0)[1] +gap ,  pos.get(1)[0]+gap , pos.get(1)[1] +gap , paint);
        canvas.drawLine(pos.get(1)[0] +gap , pos.get(1)[1]+gap  ,  pos.get(3)[0]+gap , pos.get(3)[1] +gap , paint);
        canvas.drawLine(pos.get(0)[0] +gap , pos.get(0)[1] +gap ,  pos.get(2)[0]+gap , pos.get(2)[1] +gap , paint);
        canvas.drawLine(pos.get(2)[0] +gap , pos.get(2)[1] +gap ,  pos.get(3)[0]+gap , pos.get(3)[1] +gap , paint);

    }
}
