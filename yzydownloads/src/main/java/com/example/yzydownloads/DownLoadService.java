package com.example.yzydownloads;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadService extends Service {
    /*正在下载的集合*/
    private HashMap<String, DownLoadTask> mDownLoadingTasks = new HashMap<>();
    /*线程池*/
    private ExecutorService mExecutor;
    private LinkedBlockingDeque<DownLoadEntity> mWaitingDeque = new LinkedBlockingDeque<>();
    private YzyHandler mHandler = new YzyHandler(this);



    public void checkNext(DownLoadEntity pEntity) {
        DownLoadEntity vEntity = mWaitingDeque.poll();
        if (vEntity != null) {
            startDownLoad(vEntity);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent pIntent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutor = Executors.newCachedThreadPool();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DownLoadEntity vEntity = (DownLoadEntity) intent.getSerializableExtra(Constants.KEY_DOWNLOAD_ENTITY);
        int action = intent.getIntExtra(Constants.KEY_DOWNLOAD_ACTION, -1);
        doAction(action, vEntity);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 根据action做相应的操作
     *
     * @param pAction
     * @param pEntity
     */
    private void doAction(int pAction, DownLoadEntity pEntity) {
        switch (pAction) {
            case Constants.KEY_DOWNLOAD_ACTION_ADD:
                addDownLoad(pEntity);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_PAUSE:
                pauseDownLoad(pEntity);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_RESUME:
                resumeDownLoad(pEntity);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_CANCLE:
                cancleDownLoad(pEntity);
                break;
            default:
        }
    }

    private void addDownLoad(DownLoadEntity pEntity) {
        if (mDownLoadingTasks.size() >= Constants.MAX_DOWNLOAD_TASKS_NUM) {
            mWaitingDeque.offer(pEntity);
            pEntity.status = DownLoadEntity.DownLoadStatus.waiting;
            mHandler.sendMsg(pEntity);
        } else {
            startDownLoad(pEntity);
        }
    }

    /**
     * 开始下载
     *
     * @param pEntity
     */
    private void startDownLoad(DownLoadEntity pEntity) {
        DownLoadTask vTask = new DownLoadTask(mHandler, pEntity);
        mDownLoadingTasks.put(pEntity.id, vTask);
        mExecutor.execute(vTask);
    }

    /**
     * 恢复下载
     *
     * @param pEntity
     */
    private void resumeDownLoad(DownLoadEntity pEntity) {
        addDownLoad(pEntity);
    }

    /**
     * 暂停下载
     *
     * @param pEntity
     */
    private void pauseDownLoad(DownLoadEntity pEntity) {
        /*暂停后就应该从正在下载的集合中移除*/
        DownLoadTask vTask = mDownLoadingTasks.remove(pEntity.id);
        if (vTask != null) {
            vTask.pause();
        }else {
            mWaitingDeque.remove(pEntity);
            pEntity.status = DownLoadEntity.DownLoadStatus.pause;
            mHandler.sendMsg(pEntity);
        }
    }

    /**
     * 取消下载
     *
     * @param pEntity
     */
    private void cancleDownLoad(DownLoadEntity pEntity) {
        /*暂停后就应该从正在下载的集合中移除*/
        DownLoadTask vTask = mDownLoadingTasks.remove(pEntity.id);
        if (vTask != null) {
            vTask.cancle();
        }else {
            mWaitingDeque.remove(pEntity);
            pEntity.status = DownLoadEntity.DownLoadStatus.cancle;
            mHandler.sendMsg(pEntity);
        }
    }

}
