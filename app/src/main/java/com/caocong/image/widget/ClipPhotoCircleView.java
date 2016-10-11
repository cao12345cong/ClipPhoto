package com.caocong.image.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by caocong on 10/10/16.
 * 上面的遮盖层
 */
public class ClipPhotoCircleView extends View {
    private int mRadius;
    private static final float STROKE_WIDTH = 2.0f;

    public ClipPhotoCircleView(Context context) {
        this(context, null);
    }

    public ClipPhotoCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipPhotoCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadius = Util.getRadius(getContext());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMask(canvas);
    }

    /**
     * 绘制蒙版
     */
    private void drawMask(Canvas canvas) {
        //画背景颜色
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c1 = new Canvas(bitmap);
        c1.drawARGB(150, 0, 0, 0);
        Paint strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(STROKE_WIDTH);
        c1.drawCircle(getWidth() / 2, getHeight() / 2, getRadius(), strokePaint);

        //画圆
        Bitmap circleBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c2 = new Canvas(circleBitmap);
        Paint circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.RED);
        circlePaint.setAntiAlias(true);
        c2.drawCircle(getWidth() / 2, getHeight() / 2, getRadius(), circlePaint);
        //两个图层合成
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        c1.drawBitmap(circleBitmap, 0, 0, paint);
        paint.setXfermode(null);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }


    private int getRadius() {
        return mRadius;
    }
}
