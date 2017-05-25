package com.ithomework.im.view;
import com.ithomework.im.mode.User;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23.
 */

public  interface AddFriendView {

    void onSearchResult(List<User> userList, List<String> contactsList, boolean success, String msg);

    void onAddResult(String username, boolean success, String msg);

}
