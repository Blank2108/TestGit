// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DataTools
{

    public DataTools()
    {
    }

    public static int dip2px(Context context, float f)
    {
        return (int)(0.5F + f * context.getResources().getDisplayMetrics().density);
    }

    public static int px2dip(Context context, float f)
    {
        return (int)(0.5F + f / context.getResources().getDisplayMetrics().density);
    }
}
