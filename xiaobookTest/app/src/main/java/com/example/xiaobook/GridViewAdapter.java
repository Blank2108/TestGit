// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.Context;
import android.view.*;
import android.widget.*;

public class GridViewAdapter extends BaseAdapter
{
    private final class ViewHolder
    {

        public ImageView imageView;
        public TextView textView;

    }


    public GridViewAdapter(Context context, int ai[], String as[], int i)
    {
        layout_id = 0;
        mContext = context;
        mPictures = ai;
        mTitles = as;
        mInflater = LayoutInflater.from(context);
        layout_id = i;
    }

    public int getCount()
    {
        return mPictures.length;
    }

    public Object getItem(int i)
    {
        return null;
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        int j = mPictures[i];
        String s = mTitles[i];
        ViewHolder viewholder;
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = mInflater.inflate(layout_id, null);
            viewholder.imageView = (ImageView)view.findViewById(R.id.grid_img);
            viewholder.textView = (TextView)view.findViewById(R.id.grid_tv);
            view.setTag(viewholder);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        viewholder.imageView.setBackgroundResource(j);
        viewholder.textView.setText(s);
        return view;
    }

    private int layout_id;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mPictures[];
    private String mTitles[];
}
