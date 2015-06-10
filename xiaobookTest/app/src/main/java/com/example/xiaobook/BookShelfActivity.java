// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.example.xiaobook:
//            BaseActivity, CircularImage, SlidingMenu, LoginActivity, 
//            ChannelActivity, AdminReceiver, turntest

public class BookShelfActivity extends BaseActivity
{
    private final class ItemClickListener
        implements android.widget.AdapterView.OnItemClickListener
    {

        public void onItemClick(AdapterView adapterview, View view, int i, long l)
        {
            if(i >= BookShelfActivity.mPictures.size())
            {
                Toast.makeText(getApplicationContext(), "您所打开的图书不存在！", 0).show();
                return;
            } else
            {
                Intent intent = new Intent();
                intent.setClassName("com.example.xiaobook", "com.example.xiaobook.turntest");
                intent.putExtra("bookname", i);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), (new StringBuilder()).append("").append(i).toString(), 0).show();
                return;
            }
        }

    }

    class ShlefAdapter extends BaseAdapter
    {

        public int getCount()
        {
            return 5 + BookShelfActivity.mPictures.size();
        }

        public Object getItem(int i)
        {
            return Integer.valueOf(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }


        public View getView(int position, View contentView, ViewGroup arg2) {
            // TODO Auto-generated method stub

            contentView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item1, null);

            TextView view=(TextView) contentView.findViewById(R.id.imageView1);
            if(mPictures.size()>position){
                if(position<mTitles.size()){
                    view.setText(mTitles.get(position));
                }
                view.setBackgroundResource(mPictures.get(0));
            }else{
                view.setBackgroundResource(mPictures.get(0));
                view.setClickable(false);
                view.setVisibility(View.INVISIBLE);
            }
            return contentView;
        }
    }


    public BookShelfActivity()
    {
    }

    private void activeManage()
    {
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", componentName);
        intent.putExtra("android.app.extra.ADD_EXPLANATION", "激活后就能一键锁屏了");
        startActivityForResult(intent, 9999);
    }

    private void deleteDialog(final int position, final BaseAdapter adapter)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("确认删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                BookShelfActivity.mPictures.remove(position);
                BookShelfActivity.mTitles.remove(position);
                BookShelfActivity.bookslistid.remove(position);
                adapter.notifyDataSetChanged();
                dialoginterface.dismiss();
            }

        });
        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                dialoginterface.dismiss();
            }

        });
        builder.create().show();
    }

    public void changeAppBrightness(Context context, int i)
    {
        Window window = ((Activity)context).getWindow();
        android.view.WindowManager.LayoutParams layoutparams = window.getAttributes();
        if(i == -1)
        {
            layoutparams.screenBrightness = -1F;
        } else
        {
            if(i <= 0)
                i = 1;
            layoutparams.screenBrightness = (float)i / 255F;
        }
        window.setAttributes(layoutparams);
    }

    public void dayOrNight(View view)
    {
        if(!isDay)
        {
            day_or_night.setImageResource(R.drawable.sun);
            changeAppBrightness(this, 250);
            isDay = true;
            return;
        } else
        {
            day_or_night.setImageResource(R.drawable.moon);
            changeAppBrightness(this, 20);
            isDay = false;
            return;
        }
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        if(i == 9999 && j == -1)
        {
            policyManager.lockNow();
            //finish();
        } else
        {
            //finish();
        }
        super.onActivityResult(i, j, intent);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.activity_main);
        mMenu = (SlidingMenu)findViewById(R.id.id_menu);
        bookShelf = (GridView)findViewById(R.id.bookshelf);
        final ShlefAdapter adapter = new ShlefAdapter();
        mBookShelfActivity = this;
        bookShelf.setAdapter(adapter);
        bookShelf.setOnItemClickListener(new ItemClickListener());
        bookShelf.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView adapterview, View view, int i, long l)
            {
                if(i >= BookShelfActivity.mPictures.size())
                {
                    Toast.makeText(getApplicationContext(), "你所选择的图书不存在！", 0).show();
                    return false;
                } else
                {
                    deleteDialog(i, adapter);
                    Toast.makeText(getApplicationContext(), (new StringBuilder()).append("LongClick").append(i).toString(), 0).show();
                    return true;
                }
            }

        });
        ((CircularImage)findViewById(R.id.cover_user_photo)).setImageResource(R.drawable.user_icon);
        ((CircularImage)findViewById(R.id.front_user_photo)).setImageResource(R.drawable.user_icon);
        day_or_night = (CircularImage)findViewById(R.id.day_or_night);
        addLocalBook = (RelativeLayout)findViewById(R.id.add_local_book);
        addLocalBook.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setClassName("com.example.xiaobook", "com.example.xiaobook.AddLoclBook");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        });
        userInterest = (RelativeLayout)findViewById(R.id.user_interest);
        userInterest.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), ChannelActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        userInfo = (RelativeLayout)findViewById(R.id.user_info);
        userInfo.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        takeBreak = (RelativeLayout)findViewById(R.id.take_rest);
        takeBreak.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                policyManager = (DevicePolicyManager)getSystemService("device_policy");
                componentName = new ComponentName(BookShelfActivity.this, AdminReceiver.class);
                if(policyManager.isAdminActive(componentName))
                {
                    policyManager.lockNow();
                    return;
                } else
                {
                    activeManage();
                    return;
                }
            }

        });
        aboutAuthor = (RelativeLayout)findViewById(R.id.about_author);
        aboutAuthor.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                View view1 = LayoutInflater.from(BookShelfActivity.this).inflate(R.layout.my_dialog, null);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(BookShelfActivity.this);
                builder.setView(view1);
                final AlertDialog alertdialog = builder.create();
                alertdialog.show();
                Button button = (Button)view1.findViewById(R.id.about);
                ((TextView)view1.findViewById(R.id.message)).setText(turntest.dialog_string);
                button.setOnClickListener(new View.OnClickListener()

                {

                    public void onClick(View v)

                    {
                        alertdialog.dismiss();

                    }

                });
            }


        });
        if(!isDay)
        {
            day_or_night.setImageResource(R.drawable.sun);
            changeAppBrightness(this, 250);
            isDay = true;
        } else
        {
            day_or_night.setImageResource(R.drawable.moon);
            changeAppBrightness(this, 50);
            isDay = false;
        }
        if(mTitles.size() == 0)
        {
            mPictures.add(bookCover);
            mTitles.add(bookName);
            bookslistid.add(bookId);
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if(i == 4)
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage("确定退出吗？").setCancelable(false).setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int j)
                {
                    finish();
                }


            }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int j)
                {
                    dialoginterface.cancel();
                }


            });
            builder.create().show();
            return true;
        } else
        {
            return super.onKeyDown(i, keyevent);
        }
    }

    public void showUserDetails(View view)
    {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void toggleMenu(View view)
    {
        mMenu.toggle();
    }

    public static final int CHANNELREQUEST = 1;
    public static final int CHANNELRESULT = 10;
    private static final int MY_REQUEST_CODE = 9999;
    static Integer bookCover = Integer.valueOf(R.drawable.cover_txt);
    static Integer bookId = R.raw.data1;
    static String bookName = "Demo Book";
    static ArrayList<Integer> bookslistid = new ArrayList<Integer>();
    static ArrayList<Integer> mPictures = new ArrayList<Integer>();
    static ArrayList<String> mTitles = new ArrayList<String>();
    private RelativeLayout aboutAuthor;
    private RelativeLayout addLocalBook;
    private List apps;
    private GridView bookShelf;
    private ComponentName componentName;
    private CircularImage day_or_night;
    private boolean isDay;
    private SlidingMenu mMenu;
    private DevicePolicyManager policyManager;
    private RelativeLayout takeBreak;
    private RelativeLayout userInfo;
    private RelativeLayout userInterest;
    public static BookShelfActivity mBookShelfActivity;


/*
    static DevicePolicyManager access$202(BookShelfActivity bookshelfactivity, DevicePolicyManager devicepolicymanager)
    {
        bookshelfactivity.policyManager = devicepolicymanager;
        return devicepolicymanager;
    }

*/



/*
    static ComponentName access$302(BookShelfActivity bookshelfactivity, ComponentName componentname)
    {
        bookshelfactivity.componentName = componentname;
        return componentname;
    }

*/

}
