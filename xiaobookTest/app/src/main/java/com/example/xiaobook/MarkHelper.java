// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MarkHelper extends SQLiteOpenHelper
{

    public MarkHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        PATH = "path";
        s = null;
        s = "markhelper";
    }

    public void onCreate(SQLiteDatabase sqlitedatabase)
    {
        Log.i("TAG", s);
        sqlitedatabase.execSQL((new StringBuilder()).append("CREATE TABLE ").append(s).append(" ( ").append(PATH).append(" text not null, ").append("begin int not null default 0,").append(" word text not null , time text not null);").toString());
    }

    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
    {
    }

    private static String DATABASE_NAME = "mfbook.db";
    private static int DATABASE_VERSION = 1;
    private String PATH;
    private String s;

}
