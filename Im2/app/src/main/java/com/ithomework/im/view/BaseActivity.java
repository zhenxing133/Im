package com.ithomework.im.view;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.ithomework.im.MyApplication;
import com.ithomework.im.util.ToastUtils;
import com.ithomework.im.util.Constant;
/**
 * Created by Administrator on 2017/3/2.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    private SharedPreferences mSharedPreferences;
    private MyApplication mApplication;

    public void startActivity(Class clazz,boolean isFinish){
       startActivity(clazz,isFinish,null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       //所有的Activity都依附于一个Application，在Activity中只要通过 getApplication（）方法，就能拿到当前应用中的Application对象
        mApplication = (MyApplication) getApplication();
        mApplication.addActivity(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mSharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
    }

    public void saveUser(String username,String pwd){
        mSharedPreferences.edit()
                .putString(Constant.SP_KEY_USERNAME,username)
                .putString(Constant.SP_KEY_PWD,pwd)
                .commit();
    }

    public String getUserName(){
        return mSharedPreferences.getString(Constant.SP_KEY_USERNAME,"");
    }
    public String getPwd(){
        return mSharedPreferences.getString(Constant.SP_KEY_PWD,"");
    }



    public void showDialog(String msg){
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void hideDialog(){
        mProgressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
        mApplication.removeActivity(this);
    }

    public void showToast(String msg){
        ToastUtils.showToast(this,msg);
    }

    public void startActivity(Class clazz, boolean isFinish, String contact) {
        Intent intent = new Intent(this,clazz);
        if (contact!=null){
            intent.putExtra("username",contact);
        }
        startActivity(intent);
        if (isFinish){
            finish();
        }
    }
}
