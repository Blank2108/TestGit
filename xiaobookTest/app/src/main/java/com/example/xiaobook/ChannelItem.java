// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import java.io.Serializable;

public class ChannelItem
    implements Serializable
{

    public ChannelItem()
    {
    }

    public ChannelItem(int i, String s, int j, int k)
    {
        id = Integer.valueOf(i);
        name = s;
        orderId = Integer.valueOf(j);
        selected = Integer.valueOf(k);
    }

    public int getId()
    {
        return id.intValue();
    }

    public String getName()
    {
        return name;
    }

    public int getOrderId()
    {
        return orderId.intValue();
    }

    public Integer getSelected()
    {
        return selected;
    }

    public void setId(int i)
    {
        id = Integer.valueOf(i);
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setOrderId(int i)
    {
        orderId = Integer.valueOf(i);
    }

    public void setSelected(Integer integer)
    {
        selected = integer;
    }

    public String toString()
    {
        return (new StringBuilder()).append("ChannelItem [id=").append(id).append(", name=").append(name).append(", selected=").append(selected).append("]").toString();
    }

    private static final long serialVersionUID = 0xa646dc630f49a79dL;
    public Integer id;
    public String name;
    public Integer orderId;
    public Integer selected;
}
