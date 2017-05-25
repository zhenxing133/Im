package com.ithomework.im.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.ithomework.im.view.ChatView;
import com.ithomework.im.listener.CallBackListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */

public class ChatPressenterIml implements ChatPressenter {

    private List<EMMessage> mEMMessageList = new ArrayList<>();

    private ChatView chatView;
    public ChatPressenterIml(ChatView chatView) {
        this.chatView = chatView;
    }


    @Override
    public void initChat(String contact) {
        //如果聊天过，那么获取最多最近的20条聊天记录，然后展示到View层,否则返回一个空
        updateChatData(contact);
        //chatView.onInit(mEMMessageList);
    }

    @Override
    public void updateData(String username) {
        updateChatData(username);
        chatView.onUpdate(mEMMessageList.size());

    }

    @Override
    public void sendMessage(String username, String msg) {
        EMMessage emMessage = EMMessage.createTxtSendMessage(msg,username);
        emMessage.setStatus(EMMessage.Status.INPROGRESS);
        mEMMessageList.add(emMessage);

        chatView.onUpdate(mEMMessageList.size());
        emMessage.setMessageStatusCallback(new CallBackListener() {
            @Override
            public void onMainSuccess() {
                chatView.onUpdate(mEMMessageList.size());
            }
            @Override
            public void onMainError(int i, String s) {
                chatView.onUpdate(mEMMessageList.size());
            }
        });
        EMClient.getInstance().chatManager().sendMessage(emMessage);


    }

    private void updateChatData(String contact) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(contact);
        if (conversation != null) {
            //需要将所有的未读消息标记为已读
          //  conversation.markAllMessagesAsRead();

            //曾经有聊天过
            //获取最后一条消息
            EMMessage lastMessage = conversation.getLastMessage();
            //获取最后一条消息之前的19条（最多）
            int count = 19;
            if (mEMMessageList.size()>=19){
                count = mEMMessageList.size();
            }
            List<EMMessage> messageList = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), count);
            Collections.reverse(messageList);
            mEMMessageList.clear();
            mEMMessageList.add(lastMessage);
            mEMMessageList.addAll(messageList);
            chatView.onInit(mEMMessageList);
            Collections.reverse(mEMMessageList);
        } else {
            mEMMessageList.clear();
        }
    }
}
