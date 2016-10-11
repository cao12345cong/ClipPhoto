package com.caocong.image.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by caocong on 10/10/16.
 * 布局
 */
public class ClipPhotoLayout extends FrameLayout {
    private ClipPhotoCircleView mCircleView;
    private ClipPhotoView mPhotoView;

    public ClipPhotoLayout(Context context) {
        this(context, null);
    }

    public ClipPhotoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipPhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();


    }

    private void init() {
        mCircleView = new ClipPhotoCircleView(getContext());
        mPhotoView = new ClipPhotoView(getContext());

        android.view.ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mPhotoView, lp);
        addView(mCircleView, lp);

    }

    public void setImageDrawable(Drawable drawable) {
        mPhotoView.setImageDrawable(drawable);
    }

    public void setImageDrawable(int resId) {
        setImageDrawable(getContext().getDrawable(resId));
    }

    public Bitmap clipBitmap() {
       return mPhotoView.clip();
    }
}
