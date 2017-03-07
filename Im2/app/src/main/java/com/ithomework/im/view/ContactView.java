package com.ithomework.im.view;

import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */

public interface ContactView {


    void onInitContacts(List<String> contactList);
    void updateContacts(boolean success,String msg);
    void onDelete(String contact,boolean success,String msg);
}
