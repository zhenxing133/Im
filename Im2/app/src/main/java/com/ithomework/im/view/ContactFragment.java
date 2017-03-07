package com.ithomework.im.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ithomework.im.view.BaseFragment;
import com.ithomework.im.R;
import com.ithomework.im.presenter.ContactPresenter;
import com.ithomework.im.presenter.ContactPresenterIml;
import com.ithomework.im.widget.ContactLayout;
import com.ithomework.im.adapter.ContactAdapter;
import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class ContactFragment extends BaseFragment implements ContactView{

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
    }


    @Override
    public void onInitContacts(List<String> contactList) {
        mContactAdapter = new ContactAdapter(contactList);
        mContactLayout.setAdapter(mContactAdapter);
      // mContactAdapter.setOnItemClickListener(this);
    }

    @Override
    public void updateContacts(boolean success, String msg) {

    }

    @Override
    public void onDelete(String contact, boolean success, String msg) {

    }
}
