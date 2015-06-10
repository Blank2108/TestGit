// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.zhy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.*;
import java.lang.reflect.Field;

public class ScreenUtils
{

    private ScreenUtils()
    {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int getScreenHeight(Context context)
    {
        WindowManager windowmanager = (WindowManager)context.getSystemService("window");
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowmanager.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int getScreenWidth(Context context)
    {
        WindowManager windowmanager = (WindowManager)context.getSystemService("window");
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowmanager.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static int getStatusHeight(Context context)
    {
        int j;
        try
        {
            Class class1 = Class.forName("com.android.internal.R$dimen");
            Object obj = class1.newInstance();
            int i = Integer.parseInt(class1.getField("status_bar_height").get(obj).toString());
            j = context.getResources().getDimensionPixelSize(i);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return -1;
        }
        return j;
    }

    public static Bitmap snapShotWithStatusBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, getScreenWidth(activity), getScreenHeight(activity));
        view.destroyDrawingCache();
        return bitmap;
    }

    public static Bitmap snapShotWithoutStatusBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int i = rect.top;
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, i, getScreenWidth(activity), getScreenHeight(activity) - i);
        view.destroyDrawingCache();
        return bitmap1;
    }
}
