// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class MyGridView extends GridView
{

    public MyGridView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bookshelf_layer_center);
    }

    protected void dispatchDraw(Canvas canvas)
    {
        int i = getChildCount();
        int j = 0;
        if(i > 0)
            j = getChildAt(0).getTop();
        int k = background.getWidth();
        int l = 2 + background.getHeight();
        int i1 = getWidth();
        int j1 = getHeight();
        for(int k1 = j; k1 < j1; k1 += l)
        {
            for(int l1 = 0; l1 < i1; l1 += k)
                canvas.drawBitmap(background, l1, k1, null);

        }

        super.dispatchDraw(canvas);
    }

    private Bitmap background;
}
