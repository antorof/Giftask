package es.trigit.gitftask.Vistas;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Georgevik on 24/03/15.
 */
public class SquareSpace extends View {


    public SquareSpace(Context context) {
        super(context);
    }

    public SquareSpace(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareSpace(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;

        setMeasuredDimension(width, width);
    }


}
