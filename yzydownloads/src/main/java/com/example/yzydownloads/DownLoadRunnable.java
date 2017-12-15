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
        mEntity.status = DownLoadEntity.DownLoadStatus.downloading;
        mHandler.update(mEntity);

        for (int i = mEntity.currentLength; i < mEntity.totalLength; ) {
            if (isCancle || isPause) {
                mEntity.status = isPause ? DownLoadEntity.DownLoadStatus.paused : DownLoadEntity.DownLoadStatus.cancled;
                mHandler.update(mEntity);
                // TODO: 2017/12/10
                return;
            }
            i+=10;
            mEntity.currentLength += 10;
            mHandler.update(mEntity);
            SystemClock.sleep(1000);
        }
        mEntity.status = DownLoadEntity.DownLoadStatus.complete;
        mHandler.update(mEntity);
    }
}
