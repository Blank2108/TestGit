// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{

    public StringUtils()
    {
    }

    public static boolean check2Password(String s, String s1)
    {
        return s.equals(s1);
    }

    public static boolean checkEmailInput(String s)
    {
        p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
        m = p.matcher(s);
        return m.matches();
    }

    public static boolean checkUsernameInput(String s)
    {
        return true;
    }

    public static String date2String(Date date)
    {
        if(date == null)
            return "";
        else
            return dateFormater2.format(date);
    }

    public static String date2Time(Date date)
    {
        return sqlDateFormat.format(date);
    }

    public static String date2UserId()
    {
        return dateFormatUserId.format(new Date());
    }

    public static boolean formatBoolean(String s)
    {
        return "true".equalsIgnoreCase(s);
    }

    public static String formatSoapDateTime(String s)
    {
        return s.substring(0, 19).replace("T", " ");
    }

    public static String formatSoapNullString(String s)
    {
        if(s.equals("anyType{}"))
            return "";
        else
            return s;
    }

    public static String friendly_time(String s)
    {
        Date date = toDate(s);
        String s1;
        if(date == null)
        {
            s1 = "Unknown";
        } else
        {
            s1 = "";
            Calendar calendar = Calendar.getInstance();
            if(dateFormater2.format(calendar.getTime()).equals(dateFormater2.format(date)))
            {
                int k = (int)((calendar.getTimeInMillis() - date.getTime()) / 0x36ee80L);
                if(k == 0)
                    return (new StringBuilder()).append(Math.max((calendar.getTimeInMillis() - date.getTime()) / 60000L, 1L)).append("\u5206\u949F\u524D").toString();
                else
                    return (new StringBuilder()).append(k).append("\u5C0F\u65F6\u524D").toString();
            }
            long l = date.getTime() / 0x5265c00L;
            int i = (int)(calendar.getTimeInMillis() / 0x5265c00L - l);
            if(i == 0)
            {
                int j = (int)((calendar.getTimeInMillis() - date.getTime()) / 0x36ee80L);
                if(j == 0)
                    return (new StringBuilder()).append(Math.max((calendar.getTimeInMillis() - date.getTime()) / 60000L, 1L)).append("\u5206\u949F\u524D").toString();
                else
                    return (new StringBuilder()).append(j).append("\u5C0F\u65F6\u524D").toString();
            }
            if(i == 1)
                return "\u6628\u5929";
            if(i == 2)
                return "\u524D\u5929";
            if(i > 2 && i <= 10)
                return (new StringBuilder()).append(i).append("\u5929\u524D").toString();
            if(i > 10)
                return dateFormater2.format(date);
        }
        return s1;
    }

    public static Date genCurrentDate()
    {
        Date date;
        try
        {
            String s = dateFormater.format(new Date());
            date = dateFormater.parse(s);
        }
        catch(ParseException parseexception)
        {
            parseexception.printStackTrace();
            return null;
        }
        return date;
    }

    public static boolean isEmail(String s)
    {
        if(s == null || s.trim().length() == 0)
            return false;
        else
            return emailer.matcher(s).matches();
    }

    public static boolean isEmpty(String s)
    {
        if(s != null && !"".equals(s))
        {
            int i = 0;
            while(i < s.length()) 
            {
                char c = s.charAt(i);
                if(c != ' ' && c != '\t' && c != '\r' && c != '\n')
                    return false;
                i++;
            }
        }
        return true;
    }

    public static boolean isToday(String s)
    {
        Date date = toDate(s);
        Date date1 = new Date();
        boolean flag = false;
        if(date != null)
        {
            boolean flag1 = dateFormater2.format(date1).equals(dateFormater2.format(date));
            flag = false;
            if(flag1)
                flag = true;
        }
        return flag;
    }

    public static boolean toBool(String s)
    {
        boolean flag;
        try
        {
            flag = Boolean.parseBoolean(s);
        }
        catch(Exception exception)
        {
            return false;
        }
        return flag;
    }

    public static Date toDate(String s)
    {
        Date date;
        try
        {
            date = dateFormater.parse(s);
        }
        catch(ParseException parseexception)
        {
            return null;
        }
        return date;
    }

    public static Date toDate2(String s)
    {
        Date date;
        try
        {
            date = dateFormater2.parse(s);
        }
        catch(ParseException parseexception)
        {
            return null;
        }
        return date;
    }

    public static int toInt(Object obj)
    {
        if(obj == null)
            return 0;
        else
            return toInt(obj.toString(), 0);
    }

    public static int toInt(String s, int i)
    {
        int j;
        try
        {
            j = Integer.parseInt(s);
        }
        catch(Exception exception)
        {
            return i;
        }
        return j;
    }

    public static long toLong(String s)
    {
        long l;
        try
        {
            l = Long.parseLong(s);
        }
        catch(Exception exception)
        {
            return 0L;
        }
        return l;
    }

    private static final SimpleDateFormat dateFormatUserId = new SimpleDateFormat("yyMMddkkmmss");
    private static final SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
    private static final Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private static Matcher m = null;
    private static Pattern p = null;
    private static final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

}
