package com.example.yzydownloads;

import android.content.Context;
import android.content.Intent;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadManager {
    private Context mContext;
    private static DownLoadManager instance;
    private DownLoadManager(Context pContext){
        this.mContext = pContext;
    }
    public synchronized static DownLoadManager getInstance(Context pContext){
        if(instance==null) {
            instance = new DownLoadManager(pContext);
        }
        return instance;
    }

    /**
     * 添加下载任务
     * @param pEntity
     */
    public void add(DownLoadEntity pEntity){
        Intent vIntent = new Intent(mContext,DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ENTITY ,pEntity);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_ADD);
        mContext.startService(vIntent);
    }
    public void pause(DownLoadEntity pEntity){
        Intent vIntent = new Intent(mContext,DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ENTITY ,pEntity);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_PAUSE);
        mContext.startService(vIntent);
    }
    public void resume(DownLoadEntity pEntity){
        Intent vIntent = new Intent(mContext,DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ENTITY ,pEntity);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_RESUME);
        mContext.startService(vIntent);
    }
    public void cancle(DownLoadEntity pEntity){
        Intent vIntent = new Intent(mContext,DownLoadService.class);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ENTITY ,pEntity);
        vIntent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_CANCLE);
        mContext.startService(vIntent);
    }

    public void addObserver(DataObserver pWatcher){
        DataObservable.getInstance().addObserver(pWatcher);
    }
    public void deleteObserver(DataObserver pWatcher){
        DataObservable.getInstance().deleteObserver(pWatcher);
    }
}
