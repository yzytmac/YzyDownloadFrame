package com.example.yzydownloads;

import android.os.SystemClock;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadRunnable implements Runnable{
    private YzyHandler mHandler;
    private DownLoadEntity mEntity;
    private volatile boolean isPause, isCancle;
    public DownLoadRunnable(YzyHandler pHandler, DownLoadEntity pEntity) {
        mHandler = pHandler;
        mEntity = pEntity;
    }
    public void pause() {
        isPause = true;
    }

    public void cancle() {
        isCancle = true;
    }

    @Override
    public void run() {
        for (int i = mEntity.currentLength; i < mEntity.totalLength; ) {
            if (isCancle || isPause) {
                if(isPause) {
                    mHandler.pauseStatus(mEntity);
                }
                if(isCancle) {
                    mHandler.cancleStatus(mEntity);
                }
                return;
            }
            i+=10;
            mHandler.progressStatus(mEntity,i);
            SystemClock.sleep(1000);
        }
    }
}
