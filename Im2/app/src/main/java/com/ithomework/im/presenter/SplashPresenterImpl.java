package com.ithomework.im.presenter;

import com.hyphenate.chat.EMClient;
import com.ithomework.im.view.SplashView;

/**
 * Created by Administrator on 2017/3/2.
 */

public class SplashPresenterImpl implements SplashPresenter {

    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView splashView) {
        mSplashView = splashView;
    }

    @Override
    public void checkLogined() {
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            //已经登录过了
            mSplashView.onCheckedLogin(true);
        } else {
            //还未登录
            mSplashView.onCheckedLogin(false);
        }

    }
}
