package com.ithomework.im.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
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

public class MessageFragment extends BaseFragment implements View.OnClickListener, MessageView {
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
    }

    @Override
    public void onClick(View v) {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage messages) {

        messagePressenter.initMessage();
    }

    @Override
    public void onInitMessage(List<EMConversation> mEMConversationList) {
        if (messageAdapter == null) {
            recycler_View.setLayoutManager(new LinearLayoutManager(getActivity()));
            messageAdapter = new MessageAdapter(mEMConversationList);
            recycler_View.setAdapter(messageAdapter);
        } else {
            messageAdapter.notifyDataSetChanged();
        }


    }
}
