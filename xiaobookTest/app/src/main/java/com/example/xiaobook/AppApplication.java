// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

// Referenced classes of package com.example.xiaobook:
//            SQLHelper

public class AppApplication extends Application
{

    public AppApplication()
    {
    }

    public static AppApplication getApp()
    {
        return mAppApplication;
    }

    public static void initImageLoader(Context context)
    {
        File file = StorageUtils.getOwnCacheDirectory(context, "topnews/Cache");
        Log.d("cacheDir", file.getPath());
        com.nostra13.universalimageloader.core.ImageLoaderConfiguration imageloaderconfiguration = (new com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder(context)).threadPoolSize(3).threadPriority(3).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).discCache(new UnlimitedDiscCache(file)).writeDebugLogs().build();
        ImageLoader.getInstance().init(imageloaderconfiguration);
    }

    public SQLHelper getSQLHelper()
    {
        if(sqlHelper == null)
            sqlHelper = new SQLHelper(mAppApplication);
        return sqlHelper;
    }

    public void onCreate()
    {
        super.onCreate();
        initImageLoader(getApplicationContext());
        mAppApplication = this;
    }

    public void onTerminate()
    {
        if(sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
    }

    private static AppApplication mAppApplication;
    private SQLHelper sqlHelper;
}
