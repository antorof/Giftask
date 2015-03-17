package es.trigit.gitftask.Utiles;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by DavidGSola on 14/03/2015.
 */
public class ImageViewCuadrada extends ImageView {
    public ImageViewCuadrada(Context context)
    {
        super(context);
    }

    public ImageViewCuadrada(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ImageViewCuadrada(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
