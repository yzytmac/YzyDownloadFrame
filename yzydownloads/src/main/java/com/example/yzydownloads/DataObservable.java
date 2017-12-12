package com.example.yzydownloads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by yzy on 2017/12/10.
 */

public class DataObservable extends Observable {
    private LinkedHashMap<String, DownLoadEntity> operationEntitys;
    private static DataObservable instance;

    private DataObservable() {
        operationEntitys = new LinkedHashMap<>();
    }

    public synchronized static DataObservable getInstance() {
        if (instance == null) {
            instance = new DataObservable();
        }
        return instance;
    }

    public void postStatus(DownLoadEntity pEntity) {
        operationEntitys.put(pEntity.id, pEntity);
        setChanged();
        notifyObservers(pEntity);
    }

    public ArrayList<DownLoadEntity> getPausedEntrys() {
        ArrayList<DownLoadEntity> vList = null;
        for (Map.Entry<String, DownLoadEntity> vEntry : operationEntitys.entrySet()) {
            if (vEntry.getValue().status == DownLoadEntity.DownLoadStatus.pause) {
                if (vList == null) {
                    vList = new ArrayList<>();
                }
                vList.add(vEntry.getValue());
            }
        }
        return vList;
    }
}
