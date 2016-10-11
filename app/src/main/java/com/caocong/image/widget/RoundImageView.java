package com.caocong.image.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by caocong on 10/8/16.
 * 圆角图片
 */
public class RoundImageView extends ImageView {
    private int mRadius;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadius = Util.getRadius(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        drawCirle(canvas, bitmap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                widthSize = mRadius*2;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = mRadius*2;
                break;
        }
        widthSize = heightSize = Math.max(widthSize, heightSize);
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 先画圆形，再拼接图片
     *
     * @param canvas
     * @param bitmap
     */
    private void drawCirle(Canvas canvas, Bitmap bitmap) {
        Paint paint = new Paint();
        //src,新画上去的图
        Bitmap src = bitmap;
        //dst,先画上去的图
        Bitmap mask = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //在dst上做画,先画圆形，再把图片贴上去

        //Canvas是画布,这里指定了画布的区域,画到一个Bitmap中去
        Canvas cc = new Canvas(mask);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        cc.drawARGB(0, 0, 0, 0);
        cc.drawCircle(mRadius, mRadius, mRadius, paint);
        cc.drawBitmap(mask, 0, 0, paint);

        //把新图拼上去
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        cc.drawBitmap(src, 0, 0, paint);
        paint.setXfermode(null);

        //系统画布绘制最终结果
        canvas.drawBitmap(mask, 0, 0, null);
    }


}
