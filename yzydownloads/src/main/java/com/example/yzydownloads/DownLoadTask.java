package com.example.yzydownloads;

import android.os.SystemClock;

import java.util.concurrent.ExecutorService;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadTask {
    private YzyHandler mHandler;
    private DownLoadEntity mEntity;

    private ExecutorService mExecutor;
    private DownLoadRunnable mRunnable;

    public DownLoadTask(ExecutorService pExecutor,YzyHandler pHandler, DownLoadEntity pEntity) {
        mExecutor=pExecutor;
        mHandler = pHandler;
        mEntity = pEntity;
    }

    public void pause() {
        mRunnable.pause();
    }

    public void cancle() {
        mRunnable.cancle();
    }

    public void start() {
        mRunnable = new DownLoadRunnable(mHandler,mEntity);
        mExecutor.execute(mRunnable);
    }



}
