// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.app.Dialog;
import android.content.Context;
import android.os.*;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.PrintStream;
import java.util.ArrayList;

// Referenced classes of package com.example.xiaobook:
//            MarkAdapter, MarkVo

public class MarkDialog extends Dialog
    implements android.widget.AdapterView.OnItemClickListener
{

    public MarkDialog(Context context1, ArrayList arraylist, Handler handler, int i)
    {
        super(context1, i);
        aList = null;
        list = null;
        context = context1;
        list = arraylist;
        mHandler = handler;
        dialog = this;
    }

    public static void setAdapter(ListView listview, Context context1, ArrayList arraylist)
    {
        if(arraylist.size() == 0)
        {
            dialog.dismiss();
            return;
        } else
        {
            listview.setAdapter(new MarkAdapter(context1, arraylist, listview));
            return;
        }
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.mymark);
        markList = (ListView)findViewById(R.id.markid);
        markList.setAdapter(new MarkAdapter(context, list, markList));
        markList.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView adapterview, View view, int i, long l)
    {
        dismiss();
        begin = ((MarkVo)list.get(i)).getBegin();
        System.out.println(begin);
        Message message = new Message();
        message.what = 0;
        message.arg1 = begin;
        mHandler.sendMessage(message);
    }

    public boolean onKeyUp(int i, KeyEvent keyevent)
    {
        if(i == 4)
            dismiss();
        return super.onKeyUp(i, keyevent);
    }

    private static int begin;
    private static Dialog dialog;
    private ArrayList aList;
    private Context context;
    private ArrayList list;
    private Handler mHandler;
    private ListView markList;
}
