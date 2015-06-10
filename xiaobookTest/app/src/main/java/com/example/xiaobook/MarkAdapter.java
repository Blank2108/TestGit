// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;

// Referenced classes of package com.example.xiaobook:
//            MarkVo, MarkHelper, MarkDialog

public class MarkAdapter extends BaseAdapter
{

    public MarkAdapter(Context context, ArrayList arraylist, ListView listview)
    {
        list = null;
        mContext = context;
        list = arraylist;
        markList = listview;
    }

    public int getCount()
    {
        return list.size();
    }

    public Object getItem(int i)
    {
        return Integer.valueOf(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(final int id, View view, ViewGroup viewgroup)
    {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_mark, null);
        TextView textview = (TextView)view1.findViewById(R.id.markText1);
        TextView textview1 = (TextView)view1.findViewById(R.id.markText2);
        textview.setText(((MarkVo)list.get(id)).getText());
        ((MarkVo)list.get(id)).getPage();
        ((MarkVo)list.get(id)).getCount();
        textview1.setText(((MarkVo)list.get(id)).getTime().substring(0, 16));
        ((ImageView)view1.findViewById(R.id.markImage2)).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view2)
            {
                markhelper = new MarkHelper(mContext);
                String s = ((MarkVo)list.get(id)).getBookPath();
                String s1 = ((MarkVo)list.get(id)).getTime();
                SQLiteDatabase sqlitedatabase = markhelper.getWritableDatabase();
                sqlitedatabase.delete("markhelper", (new StringBuilder()).append("path='").append(s).append("' and time ='").append(s1).append("'").toString(), null);
                sqlitedatabase.close();
                list.remove(id);
                MarkDialog.setAdapter(markList, mContext, list);
            }


        });
        return view1;
    }

    private ArrayList list;
    private Context mContext;
    private ListView markList;
    private MarkHelper markhelper;



/*
    static MarkHelper access$002(MarkAdapter markadapter, MarkHelper markhelper1)
    {
        markadapter.markhelper = markhelper1;
        return markhelper1;
    }

*/



}
