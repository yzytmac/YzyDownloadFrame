package com.example.yzydownloads;

import android.os.Handler;
import android.os.Message;

public class YzyHandler extends Handler {
    private DownLoadService mService;
    public YzyHandler(DownLoadService pDownLoadService){
        mService = pDownLoadService;
    }

    public void handleMessage(Message msg) {
        DownLoadEntity vEntity = (DownLoadEntity) msg.obj;
        switch (vEntity.status) {
            case complete:
            case cancled:
            case paused:
                mService.doNext(vEntity);
                break;
            default:
        }

        DataObservable.getInstance().postStatus(vEntity);
    }

    private void update(DownLoadEntity pEntity) {
        Message msg = obtainMessage();
        msg.obj = pEntity;
        sendMessage(msg);
    }

    public void progressStatus(DownLoadEntity pEntity, int pProgress) {
        pEntity.currentLength = pProgress;
        if (pProgress ==100) {
            pEntity.status = DownLoadEntity.DownLoadStatus.complete;
        } else {
            pEntity.status = DownLoadEntity.DownLoadStatus.downloading;
        }
        update(pEntity);
    }

    public void cancleStatus(DownLoadEntity pEntity) {
        pEntity.status = DownLoadEntity.DownLoadStatus.cancled;
        update(pEntity);
    }

    public void pauseStatus(DownLoadEntity pEntity) {
        pEntity.status = DownLoadEntity.DownLoadStatus.paused;
        update(pEntity);
    }

    public void waitStatus(DownLoadEntity pEntity) {
        pEntity.status = DownLoadEntity.DownLoadStatus.waiting;
        update(pEntity);
    }
}