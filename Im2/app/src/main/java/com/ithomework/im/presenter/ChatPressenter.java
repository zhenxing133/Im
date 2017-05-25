package com.ithomework.im.presenter;

/**
 * Created by Administrator on 2017/5/24.
 */

public interface ChatPressenter {

    void initChat(String contact);

    void updateData(String username);

    void sendMessage(String username, String msg);
}
