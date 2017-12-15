package com.example.yzydownloads;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadService extends Service {
    /*正在下载的集合*/
    private HashMap<String, DownLoadTask> mLoadingMap = new HashMap<>();
    /*等待下载的队列*/
    private LinkedBlockingDeque<DownLoadEntity> mWaitingDeque = new LinkedBlockingDeque<>();
    /*线程池*/
    private ExecutorService mExecutor;
    private YzyHandler mHandler = new YzyHandler(this);

    /**
     * 当有完成、暂停、取消时操作
     * @param pEntity
     */
    public void doNext(DownLoadEntity pEntity) {
        //当暂停、完成、取消时就要从下载队列中移除；
        mLoadingMap.remove(pEntity);
        //从等待队列中取出开始下载，
        DownLoadEntity vEntity = mWaitingDeque.poll();
        if (vEntity != null) {
            addDownLoad(vEntity);
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
        Log.e("yang", "点击前mDownLoadingTasks.size(): " + mLoadingMap.size());
        Log.e("yang", "点击前mWaitingDeque.size(): " + mWaitingDeque.size());
        doAction(action, vEntity);
        Log.e("yang", "点击后mDownLoadingTasks.size(): " + mLoadingMap.size());
        Log.e("yang", "点击后mWaitingDeque.size(): " + mWaitingDeque.size());
        return START_NOT_STICKY;
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
            case Constants.KEY_DOWNLOAD_ACTION_PAUSE_ALL:
                pauseAll();
                break;
            case Constants.KEY_DOWNLOAD_ACTION_RESUME_ALL:
                resumeAll();
                break;
            default:
        }
    }

    private void resumeAll() {
        ArrayList<DownLoadEntity> vPausedEntrys = DataObservable.getInstance().getPausedEntrys();
        if(vPausedEntrys!=null) {
            for (DownLoadEntity vEntry : vPausedEntrys) {
                addDownLoad(vEntry);
            }
        }
    }

    private void pauseAll() {
        for (DownLoadEntity vEntity : mWaitingDeque) {
            mHandler.pauseStatus(vEntity);
        }
        mWaitingDeque.clear();
        for (Map.Entry<String, DownLoadTask> vEntrySet : mLoadingMap.entrySet()) {
            vEntrySet.getValue().pause();
        }
        mLoadingMap.clear();

    }

    private void addDownLoad(DownLoadEntity pEntity) {
        if (mLoadingMap.size() >= Constants.MAX_DOWNLOAD_TASKS_NUM) {
            mWaitingDeque.offer(pEntity);
            mHandler.waitStatus(pEntity);
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
        DownLoadTask vTask = new DownLoadTask(mExecutor,mHandler, pEntity);
        mLoadingMap.put(pEntity.id, vTask);
        vTask.start();
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
        DownLoadTask vTask = mLoadingMap.remove(pEntity.id);
        if (vTask != null) {
            vTask.pause();
        }else {
            mWaitingDeque.remove(pEntity);
            mHandler.pauseStatus(pEntity);
        }
    }

    /**
     * 取消下载
     *
     * @param pEntity
     */
    private void cancleDownLoad(DownLoadEntity pEntity) {
        /*暂停后就应该从正在下载的集合中移除*/
        DownLoadTask vTask = mLoadingMap.remove(pEntity.id);
        if (vTask != null) {
            vTask.cancle();
        }else {
            mWaitingDeque.remove(pEntity);
            mHandler.cancleStatus(pEntity);
        }
    }

}
