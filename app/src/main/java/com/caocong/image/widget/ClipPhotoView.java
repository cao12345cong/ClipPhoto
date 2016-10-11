package com.caocong.image.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by caocong on 10/9/16.
 * 显示图片的view,可以托动和缩放
 */
public class ClipPhotoView extends ImageView implements View.OnTouchListener,
        ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = ClipPhotoView.class.getSimpleName();
    //最大缩放比例
    private static final float MAX_SCALE = 4.0f;
    //最小缩放比例
    private static float MIN_SCALE = 1.0f;
    //matrix array
    private static final float MATRIX_ARR[] = new float[9];

    /**
     * 状态
     */
    private static final class Mode {
        // 初始状态
        private static final int NONE = 0;
        //托动
        private static final int DRAG = 1;
        //缩放
        private static final int ZOOM = 2;
    }

    //当前状态
    private int mMode = Mode.NONE;
    //缩放手势
    private ScaleGestureDetector mScaleDetector;
    //矩阵
    private Matrix mMatrix = new Matrix();

    //托动时手指按下的点
    private PointF mPrevPointF = new PointF();

    //截取的圆框的半径
    private int mRadius;

    //第一次
    private boolean firstTime = true;

    public ClipPhotoView(Context context) {
        this(context, null);
    }

    public ClipPhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleDetector = new ScaleGestureDetector(context, this);
        mRadius = Util.getRadius(getContext());
        // 必须设置才能触发
        setOnTouchListener(this);
        setScaleType(ScaleType.MATRIX);
    }

    /**
     * 初始化
     */
    private void init() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            //throw new IllegalArgumentException("drawable can not be null");
            return;
        }
        initPosAndScale();

    }


    /**
     * 初始化缩放比例
     */
    private void initPosAndScale() {
        if (firstTime) {
            Drawable drawable = getDrawable();
            int width = getWidth();
            int height = getHeight();
            //初始化
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();

            float scaleX = 1.0f;
            float scaleY = 1.0f;
            //是否已经做过缩放处理
            boolean isScaled = false;
            if (width < getDiameter()) {
                scaleX = getDiameter() * 1.0f / width;
                isScaled = true;
            }
            if (height < getDiameter()) {
                scaleY = getDiameter() * 1.0f / height;
                isScaled = true;
            }
            float scale = Math.max(scaleX, scaleY);
            if (isScaled) {
                MIN_SCALE = scale;
            } else {
                MIN_SCALE = Math.max((getDiameter() * 1.0f) / dw, getDiameter() * 1.0f / dh) + 0.01f;
            }
            Log.d(TAG, "scale=" + scale);
            mMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            mMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
            setImageMatrix(mMatrix);
            firstTime = false;
        }

    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();
        if ((scale >= MIN_SCALE && scaleFactor > 1.0f) ||
                (scale <= MAX_SCALE && scaleFactor < 1.0f)) {
            if (scale * scaleFactor <= MIN_SCALE) {
                scaleFactor = MIN_SCALE / scale;
            } else if (scale * scaleFactor >= MAX_SCALE) {
                scaleFactor = MAX_SCALE / scale;
            }
            mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkTrans();
            setImageMatrix(mMatrix);
        }

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        mMode = Mode.ZOOM;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        mMode = Mode.NONE;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getDrawable() == null) {
            return false;
        }

        mScaleDetector.onTouchEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mMode = Mode.DRAG;
                mPrevPointF.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                mMode = Mode.NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == Mode.DRAG && event.getPointerCount() == 1) {
                    float x = event.getX();
                    float y = event.getY();
                    float dx = event.getX() - mPrevPointF.x;
                    float dy = event.getY() - mPrevPointF.y;
                    RectF rectF = getMatrixRectF();
                    // 如果宽度小于屏幕宽度，则禁止左右移动
                    if (rectF.width() <= getDiameter()) {
                        dx = 0;
                    }
                    // 如果高度小雨屏幕高度，则禁止上下移动
                    if (rectF.height() <= getDiameter()) {
                        dy = 0;
                    }
                    mMatrix.postTranslate(dx, dy);
                    checkTrans();
                    //边界判断
                    setImageMatrix(mMatrix);
                    mPrevPointF.set(x, y);
                }
                break;
        }
        return true;
    }

    /**
     * 移动边界检查
     */
    private void checkTrans() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        int horizontalPadding = (width - getDiameter()) / 2;
        int verticalPadding = (height - getDiameter()) / 2;

        // 如果宽或高大于屏幕，则控制范围 ; 这里的0.001是因为精度丢失会产生问题
        if (rect.width() + 0.01 >= getDiameter()) {
            if (rect.left > horizontalPadding) {
                deltaX = -rect.left + horizontalPadding;
            }
            if (rect.right < width - horizontalPadding) {
                deltaX = width - horizontalPadding - rect.right;
            }
        }
        if (rect.height() + 0.01 >= getDiameter()) {
            if (rect.top > verticalPadding) {
                deltaY = -rect.top + verticalPadding;
            }
            if (rect.bottom < height - verticalPadding) {
                deltaY = height - verticalPadding - rect.bottom;
            }
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }


    /**
     * 得到直径
     */
    public int getDiameter() {
        return mRadius * 2;
    }

    /**
     * 获得缩放值
     *
     * @return
     */
    private float getScale() {
        return getMatrixValue(Matrix.MSCALE_X);
    }


    private float getMatrixValue(int index) {
        mMatrix.getValues(MATRIX_ARR);
        return MATRIX_ARR[index];
    }


    /**
     * 获得Matrix的RectF
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);

        }
        return rect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();

    }

    /**
     * 截取图片
     *
     * @return
     */
    Bitmap clip() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        int x = (getWidth() - getDiameter()) / 2;
        int y = (getHeight() - getDiameter()) / 2;
        return Bitmap.createBitmap(bitmap, x, y, getDiameter(), getDiameter());
    }

}
