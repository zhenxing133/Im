package com.ithomework.im;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.ithomework.im.view.BaseActivity;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/3/1.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initHuanXin();

        //第一：默认初始化
        Bmob.initialize(this, "221fdf0f442770d9b2f19f3de75c960f");
    }

    private void initHuanXin() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }


}
