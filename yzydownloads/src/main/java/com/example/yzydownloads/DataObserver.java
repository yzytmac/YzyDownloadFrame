package com.example.yzydownloads;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by yzy on 2017/12/10.
 */

public abstract class DataObserver implements Observer {
    @Override
    public void update(Observable pObservable, Object pO) {
        if(pO instanceof DownLoadEntity) {
            notifyUpdate((DownLoadEntity) pO);
        }
    }

    public abstract void notifyUpdate(DownLoadEntity pEntity);
}
