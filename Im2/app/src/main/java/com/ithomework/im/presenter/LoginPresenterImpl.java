package com.ithomework.im.presenter;
import com.hyphenate.chat.EMClient;
import com.ithomework.im.view.LoginView;
import com.ithomework.im.listener.CallBackListener;
/**
 * Created by Administrator on 2017/3/2.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView mLoginView;

    public LoginPresenterImpl(LoginView loginView) {
        mLoginView = loginView;
    }

    @Override
    public void login(final String username, final String pwd) {
        //环信目前（3.5.x）的所有回调方法都是在子线程中回调的
        EMClient.getInstance().login(username, pwd, new CallBackListener() {

            public void onMainSuccess() {
                mLoginView.onLogin(username,pwd,true,null);

            }


            public void onMainError(int i, String s) {
                mLoginView.onLogin(username,pwd,false,s);
            }
        });

    }
}
