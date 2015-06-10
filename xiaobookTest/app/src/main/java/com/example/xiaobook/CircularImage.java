// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;

// Referenced classes of package com.example.xiaobook:
//            MaskedImage

public class CircularImage extends MaskedImage
{

    public CircularImage(Context context)
    {
        super(context);
    }

    public CircularImage(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public CircularImage(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
    }

    public Bitmap createMask()
    {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(1);
        paint.setColor(0xff000000);
        canvas.drawOval(new RectF(0.0F, 0.0F, getWidth(), getHeight()), paint);
        return bitmap;
    }
}
