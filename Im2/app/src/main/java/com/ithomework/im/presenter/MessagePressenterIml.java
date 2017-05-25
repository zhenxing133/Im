package com.ithomework.im.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.ithomework.im.presenter.MessagePressenter;
import com.ithomework.im.view.MessageFragment;
import com.ithomework.im.view.MessageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/25.
 */

public class MessagePressenterIml implements MessagePressenter {

    private MessageView messageView;
    private List<EMConversation> mEMConversationList = new ArrayList<>();

    public MessagePressenterIml(MessageView messageView) {
        this.messageView = messageView;
    }



    @Override
    public void initMessage() {
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        mEMConversationList.clear();
        mEMConversationList.addAll(allConversations.values());

        // 排序，最近的时间在最上面(时间的倒序) 回传到View层
        Collections.sort(mEMConversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {

                return (int) (o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
            }
        });
        messageView.onInitMessage(mEMConversationList);
    }


}
