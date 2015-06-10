// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;


public class UserData
{

    public UserData(String s, String s1)
    {
        userName = s;
        userPwd = s1;
    }

    public UserData(String s, String s1, int i)
    {
        userName = s;
        userPwd = s1;
        userId = i;
    }

    public int getUserId()
    {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserPwd()
    {
        return userPwd;
    }

    public void setUserId(int i)
    {
        userId = i;
    }

    public void setUserName(String s)
    {
        userName = s;
    }

    public void setUserPwd(String s)
    {
        userPwd = s;
    }

    private int userId;
    private String userName;
    private String userPwd;
}
