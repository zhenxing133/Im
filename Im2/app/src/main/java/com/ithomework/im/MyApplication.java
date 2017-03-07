package com.ithomework.im;

import android.app.Application;
import android.util.Log;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.ithomework.im.view.BaseActivity;
import com.ithomework.im.event.OnContactUpdateEvent;
import cn.bmob.v3.Bmob;
import com.ithomework.im.db.DBUtils;

import org.greenrobot.eventbus.EventBus;

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

        initDBUtils();
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
    private void initDBUtils() {
        DBUtils.initDB(this);
    }
    //监听好友状态
    private void initContactListener() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                //好友请求被同意
                //发出通知让ContactFragment更新UI
                EventBus.getDefault().post(new OnContactUpdateEvent(s, true));
            }

            @Override
            public void onContactDeleted(String s) {
                //被删除时回调此方法
                EventBus.getDefault().post(new OnContactUpdateEvent(s, false));
               // Log.d(TAG, "onContactDeleted: " + s);
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
               // Log.d(TAG, "onContactInvited: " + username + "/" + reason);
                //同意或者拒绝
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String username) {
                //增加了联系人时回调此方法
            }

            @Override
            public void onFriendRequestDeclined(String username) {
                //好友请求被拒绝
            }


            public void onContactAgreed(String s) {
                //增加了联系人时回调此方法
            }


            public void onContactRefused(String s) {
                //好友请求被拒绝
            }
        });
    }

}
