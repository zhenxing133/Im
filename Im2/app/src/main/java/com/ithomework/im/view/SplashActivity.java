package com.ithomework.im.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import com.ithomework.im.R;
import com.ithomework.im.MainActivity;
import com.ithomework.im.presenter.SplashPresenter;
import com.ithomework.im.presenter.SplashPresenterImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * Created by Administrator on 2017/3/2.
 */
public class SplashActivity extends BaseActivity implements SplashView{

    private static final long DURATION = 2000;
    private SplashPresenter mSplashPresenter;
    @InjectView(R.id.iv_splash)
    ImageView mIvSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);

        mSplashPresenter = new SplashPresenterImpl(this);

       //判断是否已经登录了,如果登录了，直接进入MainActivity,否则展示Log，进入LoginActivity

        mSplashPresenter.checkLogined();

    }

    @Override
    public void onCheckedLogin(boolean isLogined) {
        if (isLogined){
            startActivity(MainActivity.class,true);
        }else {
            //否则展示Log，进入LoginActivity
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mIvSplash, "alpha", 0, 1).setDuration(DURATION);
            alpha.start();
            alpha.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    startActivity(LoginActivity.class,true);
                }
            });

        }
    }
}
