package com.caocong.image.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by caocong on 10/10/16.
 */
public class Util {

    public static int getWidth(Context context) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        return width;
    }

    public static int getHeight(Context context) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int height = dm.heightPixels;
        return height;
    }

    public static int getRadius(Context context) {
        return Math.min(getWidth(context), getHeight(context)) / 4;
    }

}
