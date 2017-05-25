package com.ithomework.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.ithomework.im.R;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<EMConversation> mEMConversationList;

    public MessageAdapter(List<EMConversation> mEMConversationList) {
        this.mEMConversationList = mEMConversationList;
    }





    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent, false);
        MessageViewHolder messageViewHolder = new MessageViewHolder(view);
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        final EMConversation emConversation = mEMConversationList.get(position);
        //聊天的对方的名称
        String userName = emConversation.conversationId();
        //获取未读消息的个数
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        EMMessage lastMessage = emConversation.getLastMessage();
        long msgTime = lastMessage.getMsgTime();
        EMTextMessageBody lastMessageBody = (EMTextMessageBody) lastMessage.getBody();
        String lastMessageBodyMessage = lastMessageBody.getMessage();

        holder.mTvMsg.setText(lastMessageBodyMessage);
        holder.mTvUsername.setText(userName);
        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if (unreadMsgCount>99){
            holder.mTvUnread.setVisibility(View.VISIBLE);
            holder.mTvUnread.setText("99+");
        }else if (unreadMsgCount>0){
            holder.mTvUnread.setVisibility(View.VISIBLE);
            holder.mTvUnread.setText(unreadMsgCount+"");
        }else{
            holder.mTvUnread.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(emConversation);
                }
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClick(EMConversation conversation);
    }
    private OnItemClickListener mOnItemClickListener;
    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mEMConversationList==null?0:mEMConversationList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView mTvUsername;
        TextView mTvTime;
        TextView mTvMsg;
        TextView mTvUnread;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mTvUnread = (TextView) itemView.findViewById(R.id.tv_unread);
        }
    }

}
