// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;


public class MarkVo
{

    public MarkVo(String s, int i, int j, int k, String s1, String s2)
    {
        text = s;
        page = i;
        count = j;
        time = s1;
        bookPath = s2;
        begin = k;
    }

    public int getBegin()
    {
        return begin;
    }

    public String getBookPath()
    {
        return bookPath;
    }

    public long getCount()
    {
        return (long)count;
    }

    public int getPage()
    {
        return page;
    }

    public String getText()
    {
        return text;
    }

    public String getTime()
    {
        return time;
    }

    public void setBegin(int i)
    {
        begin = i;
    }

    public void setBookPath(String s)
    {
        bookPath = s;
    }

    public void setCount(int i)
    {
        count = i;
    }

    public void setPage(int i)
    {
        page = i;
    }

    public void setText(String s)
    {
        text = s;
    }

    public void setTime(String s)
    {
        time = s;
    }

    private int begin;
    private String bookPath;
    private int count;
    private int page;
    private String text;
    private String time;
}
