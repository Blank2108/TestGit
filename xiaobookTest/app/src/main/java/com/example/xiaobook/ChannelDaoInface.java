// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.ContentValues;
import java.util.List;
import java.util.Map;

// Referenced classes of package com.example.xiaobook:
//            ChannelItem

public interface ChannelDaoInface
{

    public abstract boolean addCache(ChannelItem channelitem);

    public abstract void clearFeedTable();

    public abstract boolean deleteCache(String s, String as[]);

    public abstract List listCache(String s, String as[]);

    public abstract boolean updateCache(ContentValues contentvalues, String s, String as[]);

    public abstract Map viewCache(String s, String as[]);
}
