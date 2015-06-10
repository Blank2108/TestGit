// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.example.xiaobook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

// Referenced classes of package com.example.xiaobook:
//            UserDataManager, UserData

public class LoginActivity extends Activity
{

    public LoginActivity()
    {
        mListener = new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                switch(view.getId())
                {
                default:
                    return;

                case 2131623989: 
                    register();
                    return;

                case 2131623988: 
                    login();
                    return;

                case 2131623990: 
                    cancle();
                    break;
                }
            }


        };
    }

    public void cancle()
    {
        mAccount.setText("");
        mPwd.setText("");
    }

    public boolean isUserNameAndPwdValid()
    {
        if(mAccount.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "请输入用户名", 0).show();
            return false;
        }
        if(mPwd.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "请输入密码", 0).show();
            return false;
        } else
        {
            return true;
        }
    }

    public void login()
    {
        if(isUserNameAndPwdValid())
        {
            String s = mAccount.getText().toString().trim();
            String s1 = mPwd.getText().toString().trim();
            int i = mUserDataManager.findUserByNameAndPwd(s, s1);
            if(i == 1)
            {
                loginView.setVisibility(8);
                Toast.makeText(this, "登陆成功", 0).show();
            } else
            if(i == 0)
            {
                Toast.makeText(this, "登录失败", 0).show();
                return;
            }
        }
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.loginpage);
        mAccount = (EditText)findViewById(R.id.login_edit_account);
        mAccount.setKeyListener(null);
        mPwd = (EditText)findViewById(R.id.login_edit_pwd);
        mPwd.setKeyListener(null);
        mRegisterButton = (Button)findViewById(R.id.login_btn_register);
        mLoginButton = (Button)findViewById(R.id.login_btn_login);
        mCancleButton = (Button)findViewById(R.id.login_btn_cancle);
        loginView = findViewById(R.layout.web_view);
        mRegisterButton.setOnClickListener(mListener);
        mLoginButton.setOnClickListener(mListener);
        mCancleButton.setOnClickListener(mListener);
        if(mUserDataManager == null)
        {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }

    protected void onPause()
    {
        if(mUserDataManager != null)
        {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }

    protected void onResume()
    {
        if(mUserDataManager == null)
        {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        super.onResume();
    }

    public void register()
    {
        String s;
        String s1;
label0:
        {
            if(isUserNameAndPwdValid())
            {
                s = mAccount.getText().toString().trim();
                s1 = mPwd.getText().toString().trim();
                if(mUserDataManager.findUserByName(s) <= 0)
                    break label0;
                Toast.makeText(this, "用户名已被注册，清重新输入", 0).show();
            }
            return;
        }
        UserData userdata = new UserData(s, s1);
        mUserDataManager.openDataBase();
        if(mUserDataManager.insertUserData(userdata) == -1L)
        {
            Toast.makeText(this, "注册失败", 0).show();
            return;
        } else
        {
            Toast.makeText(this, "注册成功", 0).show();
            return;
        }
    }

    private View loginView;
    private EditText mAccount;
    private Button mCancleButton;
    android.view.View.OnClickListener mListener;
    private Button mLoginButton;
    private EditText mPwd;
    private Button mRegisterButton;
    private UserDataManager mUserDataManager;
}
