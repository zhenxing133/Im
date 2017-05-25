package com.ithomework.im.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */

public interface ChatView {

    void onInit(List<EMMessage> emMessageList);

    void onUpdate(int size);


}
