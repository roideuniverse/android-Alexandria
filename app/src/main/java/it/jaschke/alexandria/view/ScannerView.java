package it.jaschke.alexandria.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import it.jaschke.alexandria.R;

/**
 * Created by roide on 12/29/15.
 * http://stackoverflow.com/questions/19947835/android-canvas-draw-transparent-circle-on-image
 */
public class ScannerView extends RelativeLayout
{
    private Paint p = new Paint();
    private Paint transparentPaint;
    Bitmap mBitmap;
    private int mViewPortSize;

    public ScannerView(Context context)
    {
        this(context, null);
    }

    public ScannerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ScannerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        Resources res = getResources();
        mViewPortSize = res.getDimensionPixelSize(R.dimen.scanner_view_port_size);
        mBitmap = Bitmap.createBitmap(mViewPortSize, mViewPortSize, Bitmap.Config.ARGB_8888);
        transparentPaint = new Paint();
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    protected void onDraw(Canvas canvas)
    {
        int left = getWidth()/2 - mViewPortSize/2;
        int top = getHeight()/2 - mViewPortSize/2;
        int right = left + mViewPortSize;
        int bottom = top + mViewPortSize;
        canvas.drawRect(left, top, right, bottom, transparentPaint);
        canvas.drawBitmap(mBitmap, 0, 0, p);
    }
}
