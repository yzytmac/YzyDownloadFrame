package com.example.yzydownloads;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by yzy on 2017/12/10.
 */

public class DataObservable extends Observable {
    private LinkedHashMap<String, DownLoadEntity> allEntitys;//维护一个所有的任务集合
    private static DataObservable instance;

    private DataObservable() {
        allEntitys = new LinkedHashMap<>();
    }

    public synchronized static DataObservable getInstance() {
        if (instance == null) {
            instance = new DataObservable();
        }
        return instance;
    }

    public void postStatus(DownLoadEntity pEntity) {
        allEntitys.put(pEntity.id, pEntity);
        setChanged();
        notifyObservers(pEntity);
    }

    public ArrayList<DownLoadEntity> getPausedEntrys() {
        ArrayList<DownLoadEntity> vList = null;
        for (Map.Entry<String, DownLoadEntity> vEntry : allEntitys.entrySet()) {
            if (vEntry.getValue().status == DownLoadEntity.DownLoadStatus.paused) {
                if (vList == null) {
                    vList = new ArrayList<>();
                }
                vList.add(vEntry.getValue());
            }
        }
        return vList;
    }
}
