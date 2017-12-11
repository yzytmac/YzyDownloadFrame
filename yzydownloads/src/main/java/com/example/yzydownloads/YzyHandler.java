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
            case cancle:
            case pause:
                mService.checkNext(vEntity);
                break;
            default:
        }

        DataObservable.getInstance().postStatus(vEntity);
    }

    public void sendMsg(DownLoadEntity pEntity) {
        Message msg = obtainMessage();
        msg.obj = pEntity;
        sendMessage(msg);
    }
}