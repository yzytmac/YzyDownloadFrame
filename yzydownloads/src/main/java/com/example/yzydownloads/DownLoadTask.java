package com.example.yzydownloads;

import android.os.SystemClock;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadTask implements Runnable {
    private DownLoadEntity mEntity;
    private boolean isPause, isCancle;

    public DownLoadTask(DownLoadEntity pEntity) {
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
        DataObservable.getInstance().postStatus(mEntity);

        mEntity.totalLength = 1024 * 100;
        for (int i = mEntity.currentLength; i < mEntity.totalLength; i++) {
            if (isCancle || isPause) {
                mEntity.status = isPause ? DownLoadEntity.DownLoadStatus.pause : DownLoadEntity.DownLoadStatus.cancle;
                DataObservable.getInstance().postStatus(mEntity);

                // TODO: 2017/12/10
                return;
            }
            i += 1024;
            mEntity.currentLength += 1024;
            DataObservable.getInstance().postStatus(mEntity);
            SystemClock.sleep(1000);
        }
        mEntity.status = DownLoadEntity.DownLoadStatus.complete;
        DataObservable.getInstance().postStatus(mEntity);
    }


}
