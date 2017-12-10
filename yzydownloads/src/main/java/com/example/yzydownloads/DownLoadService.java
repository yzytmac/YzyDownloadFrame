package com.example.yzydownloads;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadService extends Service {
    /*正在下载的集合*/
    private HashMap<String, DownLoadTask> mDownLoadingTasks = new HashMap<>();
    private ExecutorService mExecutor;

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

    private void doAction(int pAction, DownLoadEntity pEntity) {
        switch (pAction) {
            case Constants.KEY_DOWNLOAD_ACTION_ADD:
                startDownLoad(pEntity);
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

    private void cancleDownLoad(DownLoadEntity pEntity) {
        /*暂停后就应该从正在下载的集合中移除*/
        DownLoadTask vTask = mDownLoadingTasks.remove(pEntity.id);
        if (vTask != null) {
            vTask.cancle();
        }
    }

    private void resumeDownLoad(DownLoadEntity pEntity) {
        startDownLoad(pEntity);
    }

    private void pauseDownLoad(DownLoadEntity pEntity) {
        /*暂停后就应该从正在下载的集合中移除*/
        DownLoadTask vTask = mDownLoadingTasks.remove(pEntity.id);
        if (vTask != null) {
            vTask.pause();
        }
    }

    /**
     * 开始下载
     *
     * @param pEntity
     */
    private void startDownLoad(DownLoadEntity pEntity) {
        DownLoadTask vTask = new DownLoadTask(pEntity);
        mDownLoadingTasks.put(pEntity.id, vTask);
        mExecutor.execute(vTask);
    }
}
