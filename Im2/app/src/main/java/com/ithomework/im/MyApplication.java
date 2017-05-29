package com.ithomework.im;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.ithomework.im.db.DBUtils;
import com.ithomework.im.event.OnContactUpdateEvent;
import com.ithomework.im.util.ThreadUtils;
import com.ithomework.im.util.ToastUtils;
import com.ithomework.im.view.BaseActivity;
import com.ithomework.im.view.ChatActivity;
import com.ithomework.im.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/3/1.
 */

public class MyApplication extends Application {

    private SoundPool mSoundPool;
    private int mDuanSound;
    private int mYuluSound;
    private List<BaseActivity> mBaseActivityList = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        initHuanXin();

        //第一：默认初始化
        Bmob.initialize(this, "221fdf0f442770d9b2f19f3de75c960f");

        initDBUtils();
        initContactListener();
        //消息监听
        initMessageListener();
        initSoundPull();
        //监听连接状态的改变
        initConnectionListener();
    }

    public void addActivity(BaseActivity activity){
        if (!mBaseActivityList.contains(activity)){
            mBaseActivityList.add(activity);
        }
    }

    public void removeActivity(BaseActivity activity){
        mBaseActivityList.remove(activity);
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
            public void onContactAdded(String username) {
                //好友请求被同意
                //发出通知让ContactFragment更新UI
                EventBus.getDefault().post(new OnContactUpdateEvent(username, true));
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                EventBus.getDefault().post(new OnContactUpdateEvent(username, false));
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
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
        });
    }

    private void initMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                if (messages != null && messages.size() > 0) {
                    //判断当前应用，在后台发出通知栏，在前台刚发出声音
                    boolean isBackground = isRuningBackground();
                    if (isBackground) {
                        sendNotification(messages.get(0));
                        //发出长声音
                        //参数2/3：左右喇叭声音的大小
                        mSoundPool.play(mYuluSound,1,1,0,0,1);
                    } else {
                        //发出短声音
                        mSoundPool.play(mDuanSound,1,1,0,0,1);
                    }
                    EventBus.getDefault().post(messages.get(0));
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {

            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        });
    }
    //判断应用是否在后台
    private boolean isRuningBackground() {

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        if (runningTaskInfo.topActivity.getPackageName().equals(getPackageName())) {
            return false;
        } else {
            return true;
        }


    }

    private void initSoundPull() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        mDuanSound = mSoundPool.load(this, R.raw.duan, 1);
        mYuluSound = mSoundPool.load(this, R.raw.yulu, 1);
    }

    private void sendNotification(EMMessage message) {
        EMTextMessageBody messageBody = (EMTextMessageBody) message.getBody();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //延时意图
       //请求码 大于1

        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("username",message.getFrom());

        Intent[] intents = {mainIntent,chatIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(this,1,intents,PendingIntent.FLAG_UPDATE_CURRENT) ;
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true) //当点击后自动删除
                .setSmallIcon(R.drawable.message) //必须设置
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.default_avatar))
                .setContentTitle("您有一条新消息")
                .setContentText(messageBody.getMessage())
                .setContentInfo(message.getFrom())
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        notificationManager.notify(1,notification);
    }


   /* 当掉线时,Android SDK 会自动重连,无需进行任何操作,通过注册连接监听来知道连接状态。
    在聊天过程中难免会遇到网络问题,在此 SDK 为您提供了网络监听接口,实时监听
    可以根据 disconnect 返回的 error 判断原因.若服务器返回的参数值为EMError.USER_LOGIN_ANOTHER_DEVICE,
    则认为是有同一个账号异地登录；若服务器返回的参数值为EMError.USER_REMOVED,则是账号在后台被删除*/
    private void initConnectionListener() {

        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int errorCode) {

                if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    //设备在其它设备登入
                    for (BaseActivity baseActivity : mBaseActivityList) {
                        baseActivity.finish();
                    }

                    Intent intent = new Intent(MyApplication.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(MyApplication.this,"您已在其他设备上登录了，请重新登录。");
                        }
                    });
                }

            }
        });

    }

}
