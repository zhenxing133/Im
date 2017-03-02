package com.ithomework.im.listener;

import com.hyphenate.EMCallBack;
import com.ithomework.im.util.ThreadUtils;
import com.ithomework.im.util.ToastUtils;


public abstract class CallBackListener implements EMCallBack {

    public  abstract void onMainSuccess();

    public abstract void onMainError(int i, String s);

    @Override
    public void onSuccess() {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                onMainSuccess();

            }
        });
    }

    @Override
    public void onError(final int i, final String s) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                onMainError(i,s);
            }
        });
    }

    @Override
    public void onProgress(int i, String s) {
    }
}
