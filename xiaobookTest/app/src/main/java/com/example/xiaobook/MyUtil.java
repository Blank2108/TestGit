// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

public class MyUtil
{

    public MyUtil()
    {
    }

    static void fenxiang(Context context)
    {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "\u5171\u4EAB\u8F6F\u4EF6");
        intent.putExtra("android.intent.extra.TEXT", (new StringBuilder()).append("\u6211\u6B63\u5728\u770B ").append(context.getResources().getString(0x7f0b0000)).append("\uFF0C\u5F88\u597D\u770B\uFF0C\u7ED9\u4F60\u63A8\u8350\u4E0B").toString());
        intent.setFlags(0x10000000);
        context.startActivity(Intent.createChooser(intent, "\u9009\u62E9\u5206\u4EAB\u7C7B\u578B"));
    }
}
