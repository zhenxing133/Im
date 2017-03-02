package com.ithomework.im.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.ithomework.im.mode.User;
import com.ithomework.im.view.RegistView;
import com.ithomework.im.util.ThreadUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/3/2.
 */

public class RegistPresenterImpl implements RegistPresenter {

    private RegistView mRegistView;

    public RegistPresenterImpl(RegistView registView) {
        mRegistView = registView;
    }

    @Override
    public void regist(final String username, final String pwd) {

        //先注册Bmob云数据库,如果Bmob成功了再去注册环信平台
        //如果Bmob成功了，环信失败了，则再去把Bmob上的数据给删除掉

        User user = new User();
        user.setPassword(pwd);
        user.setUsername(username);

        user.signUp(new SaveListener<User>() {
            //Bmob中的回调方法都是在主线程中被调用的
            @Override
            public void done(final User user, BmobException e) {
                if (e==null){
                    //成功了再去注册环信平台
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(username, pwd);
                                //环信注册成功
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRegistView.onRegist(username,pwd,true,null);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //将Bmob上注册的user给删除掉
                                user.delete();
                                //环信注册失败了
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRegistView.onRegist(username,pwd,false,e1.toString());
                                    }
                                });
                            }
                        }
                    });
                }else {
                    //失败了，将结果告诉Activity
                    mRegistView.onRegist(username,pwd,false,e.getMessage());
                }
            }
        });



    }
}
