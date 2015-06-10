// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.*;

public class WebList extends Activity
{

    public WebList()
    {
    }

    private void init()
    {
        webView = (WebView)findViewById(0x7f0e0057);
        WebSettings websettings = webView.getSettings();
        websettings.setCacheMode(1);
        websettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://baidu.com");
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView webview, String s)
            {
                webview.loadUrl(s);
                return true;
            }


        });
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030004);
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if(i == 4)
        {
            if(webView.canGoBack())
            {
                webView.goBack();
                return true;
            }
            System.exit(0);
        }
        return super.onKeyDown(i, keyevent);
    }

    private WebView webView;
}
