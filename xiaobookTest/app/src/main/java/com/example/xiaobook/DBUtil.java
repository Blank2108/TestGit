// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Referenced classes of package com.example.xiaobook:
//            SQLHelper

public class DBUtil
{

    private DBUtil(Context context)
    {
        mContext = context;
        mSQLHelp = new SQLHelper(context);
        mSQLiteDatabase = mSQLHelp.getWritableDatabase();
    }

    public static DBUtil getInstance(Context context)
    {
        if(mInstance == null)
            mInstance = new DBUtil(context);
        return mInstance;
    }

    public void close()
    {
        mSQLHelp.close();
        mSQLHelp = null;
        mSQLiteDatabase.close();
        mSQLiteDatabase = null;
        mInstance = null;
    }

    public void deleteData(String s, String as[])
    {
        mSQLiteDatabase.delete("channel", s, as);
    }

    public void insertData(ContentValues contentvalues)
    {
        mSQLiteDatabase.insert("channel", null, contentvalues);
    }

    public Cursor selectData(String as[], String s, String as1[], String s1, String s2, String s3)
    {
        return mSQLiteDatabase.query("channel", as, s, as1, s1, s2, s3);
    }

    public void updateData(ContentValues contentvalues, String s, String as[])
    {
        mSQLiteDatabase.update("channel", contentvalues, s, as);
    }

    private static DBUtil mInstance;
    private Context mContext;
    private SQLHelper mSQLHelp;
    private SQLiteDatabase mSQLiteDatabase;
}
