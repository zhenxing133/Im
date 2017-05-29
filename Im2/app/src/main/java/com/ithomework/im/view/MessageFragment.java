package com.ithomework.im.view;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.ithomework.im.MainActivity;
import com.ithomework.im.R;
import com.ithomework.im.adapter.MessageAdapter;
import com.ithomework.im.presenter.MessagePressenter;
import com.ithomework.im.presenter.MessagePressenterIml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/3/3.
 */

public class MessageFragment extends BaseFragment implements View.OnClickListener, MessageView, MessageAdapter.OnItemClickListener {
    @InjectView(R.id.recyclerView)
    RecyclerView recycler_View;
    @InjectView(R.id.fab)
    FloatingActionButton flo_btn;
    private MessagePressenter messagePressenter;
    private MessageAdapter messageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        recycler_View.setLayoutManager(new LinearLayoutManager(getActivity()));
        flo_btn.setOnClickListener(this);
        messagePressenter = new MessagePressenterIml(this);
        messagePressenter.initMessage();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
        messageAdapter=null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

        ObjectAnimator.ofFloat(flo_btn,"rotation",0,360).setDuration(1000).start();
        Snackbar.make(recycler_View,"和我玩耍吧",Snackbar.LENGTH_LONG)
                .setAction("go go go", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage messages) {

        messagePressenter.initMessage();
    }

    @Override
    public void onInitMessage(List<EMConversation> mEMConversationList) {
        if (messageAdapter == null) {
            messageAdapter = new MessageAdapter(mEMConversationList);
            recycler_View.setAdapter(messageAdapter);
            messageAdapter.setOnItemClickListener(this);
        } else {
            messageAdapter.notifyDataSetChanged();
        }




    }

    @Override
    public void onItemClick(EMConversation conversation) {

        String username = conversation.conversationId();
        MainActivity activity = (MainActivity)getActivity();
        activity.startActivity(ChatActivity.class,false,username);
    }
}
