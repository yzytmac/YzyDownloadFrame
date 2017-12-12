package com.example.yzydownloads;

import android.os.SystemClock;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadTask implements Runnable {
    private YzyHandler mHandler;
    private DownLoadEntity mEntity;
    private volatile boolean isPause, isCancle;

    public DownLoadTask(YzyHandler pHandler, DownLoadEntity pEntity) {
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
        start();
    }

    private void start() {
        mEntity.status = DownLoadEntity.DownLoadStatus.downloading;
//        DataObservable.getInstance().postStatus(mEntity);
        mHandler.update(mEntity);

        for (int i = mEntity.currentLength; i < mEntity.totalLength; ) {
            if (isCancle || isPause) {
                mEntity.status = isPause ? DownLoadEntity.DownLoadStatus.pause : DownLoadEntity.DownLoadStatus.cancle;
//                DataObservable.getInstance().postStatus(mEntity);
                mHandler.update(mEntity);
                // TODO: 2017/12/10
                return;
            }
            i+=10;
            mEntity.currentLength += 10;
//            DataObservable.getInstance().postStatus(mEntity);
            mHandler.update(mEntity);
            SystemClock.sleep(1000);
        }
        mEntity.status = DownLoadEntity.DownLoadStatus.complete;
//        DataObservable.getInstance().postStatus(mEntity);
        mHandler.update(mEntity);
    }



}
