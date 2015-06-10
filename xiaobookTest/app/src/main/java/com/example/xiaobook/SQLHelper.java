// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper
{

    public SQLHelper(Context context1)
    {
        super(context1, "database.db", null, 1);
        context = context1;
    }

    public Context getContext()
    {
        return context;
    }

    public void onCreate(SQLiteDatabase sqlitedatabase)
    {
        sqlitedatabase.execSQL("create table if not exists channel(_id INTEGER PRIMARY KEY AUTOINCREMENT, id INTEGER , name TEXT , orderId INTEGER , selected SELECTED)");
    }

    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
    {
        onCreate(sqlitedatabase);
    }

    public static final String DB_NAME = "database.db";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ORDERID = "orderId";
    public static final String SELECTED = "selected";
    public static final String TABLE_CHANNEL = "channel";
    public static final int VERSION = 1;
    private Context context;
}
