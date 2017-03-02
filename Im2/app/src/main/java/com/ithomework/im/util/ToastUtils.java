package com.ithomework.im.util;

import android.content.Context;
import android.widget.Toast;

import static com.hyphenate.chat.a.a.a.i;

/**
 * Created by Administrator on 2017/3/2.
 */


public class ToastUtils {

    private static Toast sToast;

    public static void showToast(Context context, String msg){
        if (sToast==null){
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        //如果这个Toast已经在显示了，那么这里会立即修改文本
        sToast.setText(msg);
        sToast.show();
    }
}
