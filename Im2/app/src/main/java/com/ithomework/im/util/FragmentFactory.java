package com.ithomework.im.util;

import com.ithomework.im.view.MessageFragment;
import com.ithomework.im.view.ContactFragment;
import com.ithomework.im.view.DynamicFragment;
import com.ithomework.im.view.BaseFragment;



public class FragmentFactory {

    private static MessageFragment messageFragment;
    private static ContactFragment contactFragment;
    private static DynamicFragment dynamicFragment;

    public static BaseFragment getFragment(int position){
        BaseFragment baseFragment = null;
        switch (position) {
            case 0:
                if (messageFragment==null){
                    messageFragment = new MessageFragment();
                }
                baseFragment = messageFragment;
                break;
            case 1:
                if (contactFragment==null){
                    contactFragment = new ContactFragment();
                }
                baseFragment = contactFragment;
                break;
            case 2:
                if (dynamicFragment==null){
                    dynamicFragment = new DynamicFragment();
                }
                baseFragment = dynamicFragment;
                break;
        }
        return baseFragment;

    }

}
