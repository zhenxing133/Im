package com.ithomework.im.view;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public interface MessageView {

    void onInitMessage(List<EMConversation> mEMConversationList);
}
