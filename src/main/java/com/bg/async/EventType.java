package com.bg.async;

import javax.xml.ws.Dispatch;

/**
 * Created by Administrator on 2016/7/18.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    DISPATCH(4);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
