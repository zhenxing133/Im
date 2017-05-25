package com.ithomework.im.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ithomework.im.R;
import com.ithomework.im.util.ToastUtils;
import com.ithomework.im.presenter.ContactPresenter;
import com.ithomework.im.presenter.ContactPresenterIml;
import com.ithomework.im.widget.ContactLayout;
import com.ithomework.im.adapter.ContactAdapter;
import com.ithomework.im.event.OnContactUpdateEvent;
import com.ithomework.im.MainActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class ContactFragment extends BaseFragment implements ContactView, ContactAdapter.OnItemClickListener {

    private ContactLayout mContactLayout ;
    private ContactPresenter mContactPresenter;
    private ContactAdapter mContactAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactLayout = (ContactLayout) view.findViewById(R.id.contact_layout);
        mContactPresenter = new ContactPresenterIml(this);
        //初始化联系人
        mContactPresenter.initContact();
        //注册event事件
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnContactUpdateEvent onContactUpdateEvent){
        mContactPresenter.updateContacts();
    }




    @Override
    public void onInitContacts(List<String> contactList) {
        mContactAdapter = new ContactAdapter(contactList);
        mContactLayout.setAdapter(mContactAdapter);
        mContactAdapter.setOnItemClickListener(this);
    }

    @Override
    public void updateContacts(boolean success, String msg) {
        mContactAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDelete(String contact, boolean success, String msg) {
        if (success) {
            ToastUtils.showToast(getContext(),"删除"+contact+"成功");
        }else{
            ToastUtils.showToast(getContext(),"删除失败");
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
    //长按事件
    @Override
    public void onItemLongClick(final String contact, int position) {

        Snackbar.make(mContactLayout,"确定要删除"+contact+"么",Snackbar.LENGTH_LONG)
                .setAction("确定",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(getContext(),"删除");
                mContactPresenter.deleteContact(contact);
            }
        }).show();
    }

    @Override
    public void onItemClick(String contact, int position) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.startActivity(ChatActivity.class,false,contact);
    }
}
