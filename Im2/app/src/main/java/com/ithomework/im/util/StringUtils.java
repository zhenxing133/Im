package com.ithomework.im.util;

import android.text.TextUtils;


/**
 * Created by Administrator on 2017/3/2.
 */

public class StringUtils {

    public static boolean checkUsername(String username){
        if (TextUtils.isEmpty(username)){
            return false;
        }

        return username.matches("^[a-zA-Z]\\w{2,19}$");
    }

    public static  boolean checkPwd(String pwd){
        if (TextUtils.isEmpty(pwd)){
            return false;
        }
        return  pwd.matches("^[0-9]{3,20}$");
    }

    public static  String getInitial(String contact){
        if (TextUtils.isEmpty(contact)){
            return contact;
        }else {
            return contact.substring(0,1).toUpperCase();
        }
    }

}
