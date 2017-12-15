package com.example.yzydownloads;

import android.content.Context;
import android.content.Intent;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadManager {
    private Context mContext;
    private static DownLoadManager instance;

    private DownLoadManager(Context pContext) {
        this.mContext = pContext;
    }

    public synchronized static DownLoadManager getInstance(Context pContext) {
        if (instance == null) {
            instance = new DownLoadManager(pContext);
        }
        return instance;
    }

    /**
     * 添加下载任务
     *
     * @param pEntity
     */
    public void add(DownLoadEntity pEntity) {
        Intent vIntent = new Intent(mContext, DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ENTITY, pEntity);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_ADD);
        mContext.startService(vIntent);
    }

    /**
     * 暂停下载
     *
     * @param pEntity
     */
    public void pause(DownLoadEntity pEntity) {
        Intent vIntent = new Intent(mContext, DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ENTITY, pEntity);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_PAUSE);
        mContext.startService(vIntent);
    }

    /**
     * 暂停全部
     */
    public void pauseAll() {
        Intent vIntent = new Intent(mContext, DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_PAUSE_ALL);
        mContext.startService(vIntent);
    }

    /**
     * 恢复下载
     *
     * @param pEntity
     */
    public void resume(DownLoadEntity pEntity) {
        Intent vIntent = new Intent(mContext, DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ENTITY, pEntity);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_RESUME);
        mContext.startService(vIntent);
    }

    /**
     * 恢复全部
     */
    public void resumeAll() {
        Intent vIntent = new Intent(mContext, DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_RESUME_ALL);
        mContext.startService(vIntent);
    }

    /**
     * 取消全部
     */
    public void cancleAll() {
        Intent vIntent = new Intent(mContext, DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_CANCLE_ALL);
        mContext.startService(vIntent);
    }

    /**
     * 取消下载
     *
     * @param pEntity
     */
    public void cancle(DownLoadEntity pEntity) {
        Intent vIntent = new Intent(mContext, DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ENTITY, pEntity);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_CANCLE);
        mContext.startService(vIntent);
    }

    /**
     * 添加监听器
     *
     * @param pObserver
     */
    public void addObserver(DataObserver pObserver) {
        DataObservable.getInstance().addObserver(pObserver);
    }

    /**
     * 产出监听器
     *
     * @param pObserver
     */
    public void deleteObserver(DataObserver pObserver) {
        DataObservable.getInstance().deleteObserver(pObserver);
    }
}
