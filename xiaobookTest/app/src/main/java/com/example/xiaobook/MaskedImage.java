// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.io.PrintStream;

public abstract class MaskedImage extends ImageView
{

    public MaskedImage(Context context)
    {
        super(context);
    }

    public MaskedImage(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public MaskedImage(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
    }

    public abstract Bitmap createMask();

    protected void onDraw(Canvas canvas)
    {
        Drawable drawable = getDrawable();
        if(drawable == null)
            return;
        try
        {
            if(paint == null)
            {
                paint = new Paint();
                paint.setFilterBitmap(false);
                paint.setXfermode(MASK_XFERMODE);
            }
            int i = canvas.saveLayer(0.0F, 0.0F, getWidth(), getHeight(), null, 31);
            drawable.setBounds(0, 0, getWidth(), getHeight());
            drawable.draw(canvas);
            if(mask == null || mask.isRecycled())
                mask = createMask();
            canvas.drawBitmap(mask, 0.0F, 0.0F, paint);
            canvas.restoreToCount(i);
            return;
        }
        catch(Exception exception)
        {
            StringBuilder stringbuilder = (new StringBuilder()).append("Attempting to draw with recycled bitmap. View ID = ");
            System.out.println((new StringBuilder()).append("localStringBuilder==").append(stringbuilder).toString());
            return;
        }
    }

    private static final Xfermode MASK_XFERMODE;
    private Bitmap mask;
    private Paint paint;

    static 
    {
        MASK_XFERMODE = new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN);
    }
}
