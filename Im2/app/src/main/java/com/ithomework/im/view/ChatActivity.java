package com.ithomework.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.ithomework.im.R;
import com.ithomework.im.presenter.ChatPressenter;
import com.ithomework.im.presenter.ChatPressenterIml;
import com.ithomework.im.adapter.ChatAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/24.
 */

public class ChatActivity extends BaseActivity implements TextWatcher ,ChatView{

    @InjectView(R.id.tv_title)
    TextView tv_Title;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.et_msg)
    EditText et_text;
    @InjectView(R.id.btn_send)
    Button btn_Send;
    private ChatPressenter chatPressenter;
    private ChatAdapter chatAdapter;
    private String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
         username = intent.getStringExtra("username");
        if (TextUtils.isEmpty(username)) {
            finish();
            return ;
        }
        tv_Title.setText("跟"+username+"聊天中");
        et_text.addTextChangedListener(this);
        String msg = et_text.getText().toString();
        if (TextUtils.isEmpty(msg)){
            btn_Send.setEnabled(false);
        }else {
            btn_Send.setEnabled(true);
        }
        chatPressenter = new ChatPressenterIml(this);
        chatPressenter.initChat(username);
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage message){
        //showToast("p p p p p ");
        //收到消息后
        //判断当前这个消息是不是正在聊天的用户给我发的， 如果是，让ChatPresenter 更新数据
        String from = message.getFrom();
        if (from.equals(username)){
            chatPressenter.updateData(username);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    @OnClick(R.id.btn_send)
    public void onClick() {
        //showToast("我被发送了");
        String msg = et_text.getText().toString();
        chatPressenter.sendMessage(username,msg);
        et_text.getText().clear();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length()==0){
            btn_Send.setEnabled(false);
        }else{
            btn_Send.setEnabled(true);
        }
    }

    @Override
    public void onInit(List<EMMessage> emMessageList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(emMessageList);
            recyclerView.setAdapter(chatAdapter);
        } else {
            chatAdapter.notifyDataSetChanged();
        }


        if (emMessageList.size()!=0){
            recyclerView.scrollToPosition(emMessageList.size()-1);
        }
    }

    @Override
    public void onUpdate(int size) {
        chatAdapter.notifyDataSetChanged();
        if (size!=0){
            recyclerView.smoothScrollToPosition(size-1);
        }
    }


}
