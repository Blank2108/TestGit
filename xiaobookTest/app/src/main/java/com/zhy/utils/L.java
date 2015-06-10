// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.zhy.utils;

import android.util.Log;

public class L
{

    private L()
    {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void d(String s)
    {
        if(isDebug)
            Log.d("way", s);
    }

    public static void d(String s, String s1)
    {
        if(isDebug)
            Log.i(s, s1);
    }

    public static void e(String s)
    {
        if(isDebug)
            Log.e("way", s);
    }

    public static void e(String s, String s1)
    {
        if(isDebug)
            Log.i(s, s1);
    }

    public static void i(String s)
    {
        if(isDebug)
            Log.i("way", s);
    }

    public static void i(String s, String s1)
    {
        if(isDebug)
            Log.i(s, s1);
    }

    public static void v(String s)
    {
        if(isDebug)
            Log.v("way", s);
    }

    public static void v(String s, String s1)
    {
        if(isDebug)
            Log.i(s, s1);
    }

    private static final String TAG = "way";
    public static boolean isDebug = true;

}
