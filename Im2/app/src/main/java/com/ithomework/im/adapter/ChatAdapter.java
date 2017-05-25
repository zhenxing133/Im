package com.ithomework.im.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.ithomework.im.R;
import com.hyphenate.chat.EMMessage;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<EMMessage> emMessageList;
    public ChatAdapter(List<EMMessage> emMessageList) {
        this.emMessageList = emMessageList;
    }

    @Override
    public int getItemCount() {
        return emMessageList==null?0:emMessageList.size();
    }

     @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = emMessageList.get(position);

        return emMessage.direct()== EMMessage.Direct.RECEIVE?0:1;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_receiver, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_send, parent, false);
        }
        ChatViewHolder chatViewHolder = new ChatViewHolder(view);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        EMMessage emMessage = emMessageList.get(position);
        long msgTime = emMessage.getMsgTime();
        //需要将消息body转换为EMTextMessageBody
        EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
        String message = body.getMessage();
        holder.mTvMsg.setText(message);

        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if (position==0){
            holder.mTvTime.setVisibility(View.VISIBLE);
        }else{
            EMMessage preMessage = emMessageList.get(position - 1);
            long preMsgTime = preMessage.getMsgTime();
            if (DateUtils.isCloseEnough(msgTime,preMsgTime)){
                holder.mTvTime.setVisibility(View.GONE);
            }else{
                holder.mTvTime.setVisibility(View.VISIBLE);
            }
        }
        if (emMessage.direct()== EMMessage.Direct.SEND){
            switch (emMessage.status()) {
                case INPROGRESS:
                    holder.mIvState.setVisibility(View.VISIBLE);
                    holder.mIvState.setImageResource(R.drawable.msg_state_animation);
                    AnimationDrawable drawable = (AnimationDrawable) holder.mIvState.getDrawable();
                    if (drawable.isRunning()){
                        drawable.stop();
                    }
                    drawable.start();
                    break;
                case SUCCESS:
                    holder.mIvState.setVisibility(View.GONE);
                    break;
                case FAIL:
                    holder.mIvState.setVisibility(View.VISIBLE);
                    holder.mIvState.setImageResource(R.drawable.msg_error);
                    break;
            }
        }
    }




    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView mTvTime;
        TextView mTvMsg;
        ImageView mIvState;
        public ChatViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mIvState = (ImageView) itemView.findViewById(R.id.iv_state);
        }
    }
}
