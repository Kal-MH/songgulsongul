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
    float lineThickness = 30.0f;
    ArrayList<float[]> pos;
    int gap;
    public DrawRect(Context context) {
        super(context);
        paint = new Paint();
        gap = 100;
    }

    public DrawRect(Context context, ArrayList<float[]> pos){
        super(context);
        paint = new Paint();
        this.pos = pos;
        gap = 100;
    }

    public DrawRect(Context context, ArrayList<float[]> pos, int gap){
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

        canvas.drawLine(pos.get(0)[0] +gap , pos.get(0)[1] +gap ,  pos.get(1)[0]+gap , pos.get(1)[1] +gap , paint);
        canvas.drawLine(pos.get(1)[0] +gap , pos.get(1)[1]+gap  ,  pos.get(3)[0]+gap , pos.get(3)[1] +gap , paint);
        canvas.drawLine(pos.get(0)[0] +gap , pos.get(0)[1] +gap ,  pos.get(2)[0]+gap , pos.get(2)[1] +gap , paint);
        canvas.drawLine(pos.get(2)[0] +gap , pos.get(2)[1] +gap ,  pos.get(3)[0]+gap , pos.get(3)[1] +gap , paint);


    }
}
