package com.ithomework.im;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ithomework.im.util.FragmentFactory;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ithomework.im.view.BaseActivity;
import com.ithomework.im.view.BaseFragment;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.fl_content)
    FrameLayout flContent;
    @InjectView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @InjectView(R.id.activity_main)
    LinearLayout activityMain;
    private int[] titleIds = {R.string.conversation,R.string.contact,R.string.plugin};
    private BadgeItem mBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initToolBar();
        initBottomNavigation();
        initFirstFragment();
    }

    private void initToolBar() {

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tvTitle.setText("消息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //图标显示
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuBuilder builder = (MenuBuilder) menu;
        builder.setOptionalIconsVisible(true);
        return true;
    }
    //toolbar监听
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_friend:
               // startActivity(AddFriendActivity.class,false);
                break;
            case R.id.menu_scan:
                showToast("分享好友");

                break;
            case R.id.menu_about:
                showToast("关于我们");
                break;
            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

    private void initBottomNavigation() {
        BottomNavigationItem conversationItem = new BottomNavigationItem(R.drawable.conversation_selected_2,"消息");
        mBadgeItem = new BadgeItem();
        mBadgeItem.setGravity(Gravity.RIGHT);
        mBadgeItem.setTextColor("#ffffff");
        mBadgeItem.setBackgroundColor("#ff0000");
        mBadgeItem.setText("5");
        mBadgeItem.show();

        conversationItem.setBadgeItem(mBadgeItem);
        //单个设置
        //conversationItem.setActiveColor(getResources().getColor(R.color.btn_normal));//选中的颜色
        //conversationItem.setInActiveColor(getColoretResources().getColor(R.color.inActive));//没选中的颜色
        mBottomNavigationBar.addItem(conversationItem);

        BottomNavigationItem contactItem = new BottomNavigationItem(R.drawable.contact_selected_2,"联系人");
        mBottomNavigationBar.addItem(contactItem);

        BottomNavigationItem pluginItem = new BottomNavigationItem(R.drawable.plugin_selected_2,"动态");
        mBottomNavigationBar.addItem(pluginItem);

        mBottomNavigationBar.setActiveColor(R.color.btn_normal);
        mBottomNavigationBar.setInActiveColor(R.color.inActive);
        mBottomNavigationBar.initialise();//代表初始化完成
        mBottomNavigationBar.setTabSelectedListener(this);//监听
    }



    @Override
    public void onTabSelected(int position) {

         //先判断当前Fragment是否被添加到了MainActivity中
         //如果添加了则直接显示即可,如果没有添加则添加，然后显示
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = FragmentFactory.getFragment(position);
        if (!fragment.isAdded()){
            transaction.add(R.id.fl_content,fragment,""+position);
        }
        transaction.show(fragment).commit();

        tvTitle.setText(titleIds[position]);
    }

    private void initFirstFragment() {

        //如果这个Activity中已经有老（就是Activity保存的历史的状态，又恢复了）的Fragment，先全部移除
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for(int i=0;i<titleIds.length;i++){
            Fragment fragment = supportFragmentManager.findFragmentByTag(i + "");
            if (fragment!=null){
                fragmentTransaction.remove(fragment);
            }
        }
        fragmentTransaction.commit();

        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, FragmentFactory.getFragment(0),"0").commit();

        tvTitle.setText(R.string.conversation);
    }

    @Override
    public void onTabUnselected(int position) {
        getSupportFragmentManager().beginTransaction().hide(FragmentFactory.getFragment(position)).commit();
    }

    @Override
    public void onTabReselected(int position) {
        showToast("又选中了"+position);
    }
}
