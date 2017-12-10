package com.example.yzydownloads;

import java.util.Observable;

/**
 * Created by yzy on 2017/12/10.
 */

public class DataObservable extends Observable {
    private static DataObservable instance;
    private DataObservable() {
    }
    public synchronized static DataObservable getInstance(){
        if(instance == null) {
            instance = new DataObservable();
        }
        return instance;
    }

    public void postStatus(DownLoadEntity pEntity){
        setChanged();
        notifyObservers(pEntity);
    }
}
