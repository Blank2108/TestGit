// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Referenced classes of package com.example.xiaobook:
//            UserData

public class UserDataManager
{
    private static class DataBaseManagementHelper extends SQLiteOpenHelper
    {

        public void onCreate(SQLiteDatabase sqlitedatabase)
        {
            Log.i("UserDataManager", (new StringBuilder()).append("db.getVersion()=").append(sqlitedatabase.getVersion()).toString());
            sqlitedatabase.execSQL("DROP TABLE IF EXISTS users;");
            sqlitedatabase.execSQL("CREATE TABLE users (_id integer primary key,user_name varchar,user_pwd varchar);");
            Log.i("UserDataManager", "db.execSQL(DB_CREATE)");
            Log.e("UserDataManager", "CREATE TABLE users (_id integer primary key,user_name varchar,user_pwd varchar);");
        }

        public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
        {
            Log.i("UserDataManager", "DataBaseManagementHelper onUpgrade");
            onCreate(sqlitedatabase);
        }

        DataBaseManagementHelper(Context context)
        {
            super(context, "user_data", null, 2);
        }
    }


    public UserDataManager(Context context)
    {
        mContext = null;
        mSQLiteDatabase = null;
        mDatabaseHelper = null;
        mContext = context;
        Log.i("UserDataManager", "UserDataManager construction!");
    }

    public void closeDataBase()
        throws SQLException
    {
        mDatabaseHelper.close();
    }

    public boolean deleteAllUserDatas()
    {
        return mSQLiteDatabase.delete("users", null, null) > 0;
    }

    public boolean deleteUserData(int i)
    {
        return mSQLiteDatabase.delete("users", (new StringBuilder()).append("_id=").append(i).toString(), null) > 0;
    }

    public Cursor fetchAllUserDatas()
    {
        return mSQLiteDatabase.query("users", null, null, null, null, null, null);
    }

    public Cursor fetchUserData(int i)
        throws SQLException
    {
        Cursor cursor = mSQLiteDatabase.query(false, "users", null, (new StringBuilder()).append("_id=").append(i).toString(), null, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public int findUserByName(String s)
    {
        Log.i("UserDataManager", (new StringBuilder()).append("findUserByName , userName=").append(s).toString());
        Cursor cursor = mSQLiteDatabase.query("users", null, (new StringBuilder()).append("user_name=").append(s).toString(), null, null, null, null);
        int i = 0;
        if(cursor != null)
        {
            i = cursor.getCount();
            cursor.close();
            Log.i("UserDataManager", (new StringBuilder()).append("findUserByName , result=").append(i).toString());
        }
        return i;
    }

    public int findUserByNameAndPwd(String s, String s1)
    {
        Log.i("UserDataManager", "findUserByNameAndPwd");
        Cursor cursor = mSQLiteDatabase.query("users", null, (new StringBuilder()).append("user_name=").append(s).append(" and ").append("user_pwd").append("=").append(s1).toString(), null, null, null, null);
        int i = 0;
        if(cursor != null)
        {
            i = cursor.getCount();
            cursor.close();
            Log.i("UserDataManager", (new StringBuilder()).append("findUserByNameAndPwd , result=").append(i).toString());
        }
        return i;
    }

    public String getStringByColumnName(String s, int i)
    {
        Cursor cursor = fetchUserData(i);
        String s1 = cursor.getString(cursor.getColumnIndex(s));
        cursor.close();
        return s1;
    }

    public long insertUserData(UserData userdata)
    {
        String s = userdata.getUserName();
        String s1 = userdata.getUserPwd();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("user_name", s);
        contentvalues.put("user_pwd", s1);
        return mSQLiteDatabase.insert("users", "_id", contentvalues);
    }

    public void openDataBase()
        throws SQLException
    {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public boolean updateUserData(UserData userdata)
    {
        int i = userdata.getUserId();
        String s = userdata.getUserName();
        String s1 = userdata.getUserPwd();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("user_name", s);
        contentvalues.put("user_pwd", s1);
        return mSQLiteDatabase.update("users", contentvalues, (new StringBuilder()).append("_id=").append(i).toString(), null) > 0;
    }

    public boolean updateUserDataById(String s, int i, String s1)
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(s, s1);
        return mSQLiteDatabase.update("users", contentvalues, (new StringBuilder()).append("_id=").append(i).toString(), null) > 0;
    }

    private static final String DB_CREATE = "CREATE TABLE users (_id integer primary key,user_name varchar,user_pwd varchar);";
    private static final String DB_NAME = "user_data";
    private static final int DB_VERSION = 2;
    public static final String ID = "_id";
    public static final String SILENT = "silent";
    private static final String TABLE_NAME = "users";
    private static final String TAG = "UserDataManager";
    public static final String USER_NAME = "user_name";
    public static final String USER_PWD = "user_pwd";
    public static final String VIBRATE = "vibrate";
    private Context mContext;
    private DataBaseManagementHelper mDatabaseHelper;
    private SQLiteDatabase mSQLiteDatabase;
}
