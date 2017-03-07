package com.ithomework.im.event;

/**
 * Created by Administrator on 2017/3/7.
 */

public class OnContactUpdateEvent {
    public String contact;
    public boolean isAdded;

    public OnContactUpdateEvent(String contact, boolean isAdded) {
        this.contact = contact;
        this.isAdded = isAdded;
    }
}
