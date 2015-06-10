// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.database.SQLException;
import android.util.Log;
import java.util.*;

// Referenced classes of package com.example.xiaobook:
//            ChannelItem, ChannelDao, SQLHelper

public class ChannelManage
{

    private ChannelManage(SQLHelper sqlhelper)
        throws SQLException
    {
        userExist = false;
        if(channelDao == null)
            channelDao = new ChannelDao(sqlhelper.getContext());
    }

    public static ChannelManage getManage(SQLHelper sqlhelper)
        throws SQLException
    {
        if(channelManage == null)
            channelManage = new ChannelManage(sqlhelper);
        return channelManage;
    }

    private void initDefaultChannel()
    {
        Log.d("deleteAll", "deleteAll");
        deleteAllChannel();
        saveUserChannel(defaultUserChannels);
        saveOtherChannel(defaultOtherChannels);
    }

    public void deleteAllChannel()
    {
        channelDao.clearFeedTable();
    }

    public List getOtherChannel()
    {
label0:
        {
            List list = channelDao.listCache("selected= ?", new String[] {
                "0"
            });
            ArrayList arraylist = new ArrayList();
            if(list != null && !((List)list).isEmpty())
            {
                List list1 = (List)list;
                int i = list1.size();
                for(int j = 0; j < i; j++)
                {
                    ChannelItem channelitem = new ChannelItem();
                    channelitem.setId(Integer.valueOf((String)((Map)list1.get(j)).get("id")).intValue());
                    channelitem.setName((String)((Map)list1.get(j)).get("name"));
                    channelitem.setOrderId(Integer.valueOf((String)((Map)list1.get(j)).get("orderId")).intValue());
                    channelitem.setSelected(Integer.valueOf((String)((Map)list1.get(j)).get("selected")));
                    arraylist.add(channelitem);
                }

            } else
            if(!userExist)
                break label0;
            return arraylist;
        }
        return (List)defaultOtherChannels;
    }

    public List getUserChannel()
    {
        List list = channelDao.listCache("selected= ?", new String[] {
            "1"
        });
        Object obj;
        if(list != null && !((List)list).isEmpty())
        {
            userExist = true;
            List list1 = (List)list;
            int i = list1.size();
            obj = new ArrayList();
            for(int j = 0; j < i; j++)
            {
                ChannelItem channelitem = new ChannelItem();
                channelitem.setId(Integer.valueOf((String)((Map)list1.get(j)).get("id")).intValue());
                channelitem.setName((String)((Map)list1.get(j)).get("name"));
                channelitem.setOrderId(Integer.valueOf((String)((Map)list1.get(j)).get("orderId")).intValue());
                channelitem.setSelected(Integer.valueOf((String)((Map)list1.get(j)).get("selected")));
                ((List) (obj)).add(channelitem);
            }

        } else
        {
            initDefaultChannel();
            obj = defaultUserChannels;
        }
        return ((List) (obj));
    }

    public void saveOtherChannel(List list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            ChannelItem channelitem = (ChannelItem)list.get(i);
            channelitem.setOrderId(i);
            channelitem.setSelected(Integer.valueOf(0));
            channelDao.addCache(channelitem);
        }

    }

    public void saveUserChannel(List list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            ChannelItem channelitem = (ChannelItem)list.get(i);
            channelitem.setOrderId(i);
            channelitem.setSelected(Integer.valueOf(1));
            channelDao.addCache(channelitem);
        }

    }

    public static ChannelManage channelManage;
    public static List defaultOtherChannels;
    public static List defaultUserChannels;
    private ChannelDao channelDao;
    private boolean userExist;

    static 
    {
        defaultUserChannels = new ArrayList();
        defaultOtherChannels = new ArrayList();
        defaultUserChannels.add(new ChannelItem(1, "\u63A8\u8350", 1, 1));
        defaultUserChannels.add(new ChannelItem(2, "hot Topic", 2, 1));
        defaultUserChannels.add(new ChannelItem(3, "\u676D\u5DDE", 3, 1));
        defaultUserChannels.add(new ChannelItem(4, "\u65F6\u5C1A", 4, 1));
        defaultUserChannels.add(new ChannelItem(5, "\u79D1\u6280", 5, 1));
        defaultUserChannels.add(new ChannelItem(6, "\u4F53\u80B2", 6, 1));
        defaultUserChannels.add(new ChannelItem(7, "\u519B\u4E8B", 7, 1));
        defaultOtherChannels.add(new ChannelItem(8, "\u8D22\u7ECF", 1, 0));
        defaultOtherChannels.add(new ChannelItem(9, "\u6C7D\u8F66", 2, 0));
        defaultOtherChannels.add(new ChannelItem(10, "\u623F\u4EA7", 3, 0));
        defaultOtherChannels.add(new ChannelItem(11, "\u793E\u4F1A", 4, 0));
        defaultOtherChannels.add(new ChannelItem(12, "\u60C5\u611F", 5, 0));
        defaultOtherChannels.add(new ChannelItem(13, "\u5973\u4EBA", 6, 0));
        defaultOtherChannels.add(new ChannelItem(14, "\u65C5\u6E38", 7, 0));
        defaultOtherChannels.add(new ChannelItem(15, "\u5065\u5EB7", 8, 0));
        defaultOtherChannels.add(new ChannelItem(16, "\u7F8E\u5973", 9, 0));
        defaultOtherChannels.add(new ChannelItem(17, "\u6E38\u620F", 10, 0));
        defaultOtherChannels.add(new ChannelItem(18, "\u6570\u7801", 11, 0));
        defaultUserChannels.add(new ChannelItem(19, "\u5A31\u4E50", 12, 0));
    }
}
