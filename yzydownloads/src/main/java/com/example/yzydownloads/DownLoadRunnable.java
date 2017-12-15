package com.example.yzydownloads;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadRunnable implements Runnable {
    private YzyHandler mHandler;
    private DownLoadEntity mEntity;
    private volatile boolean isPause, isCancle;
    private int mTotalLength = 0;
    private boolean mIsSupportRange;
    private URL mUrl;
    private File mFile;

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
        mIsSupportRange = checkIsSupportRange();
        File dir = new File(mEntity.localPath);
        mFile = new File(mEntity.localPath, mEntity.name);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
            } catch (IOException pE) {
                pE.printStackTrace();
            }
        }
        if (mFile.length() == mTotalLength) {
            mHandler.progressStatus(mEntity, 100);
            return;
        }
        download();
    }

    private void download() {
        InputStream is = null;
        FileOutputStream os = null;
        RandomAccessFile vAccessFile = null;
        long lastTimeMillis = 0;
        try {
            HttpURLConnection vConnection = (HttpURLConnection) mUrl.openConnection();
            vConnection.setRequestMethod("GET");

            if (mIsSupportRange) {
                vConnection.setRequestProperty("Range", "bytes=" + mFile.length() + "-");
                vAccessFile = new RandomAccessFile(mFile, "rw");
                vAccessFile.seek(mFile.length());
            } else {
                os = new FileOutputStream(mFile);
            }

            vConnection.connect();
            is = vConnection.getInputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                if (isCancle || isPause) {
                    if (isPause) {
                        mHandler.pauseStatus(mEntity);
                    }
                    if (isCancle) {
                        mHandler.cancleStatus(mEntity);
                    }
                    return;
                }
                if (mIsSupportRange) {
                    vAccessFile.write(buffer, 0, len);
                } else {
                    os.write(buffer, 0, len);
                }
                long progress = mFile.length() * 100 / mTotalLength;
                Log.e("yzy", "singleThreadDownLoad: " + progress);
                long vCurrentTimeMillis = System.currentTimeMillis();
                //一定要延时处理，否则频繁会频繁刷新listview，会导致点击时间失效
                if (vCurrentTimeMillis - lastTimeMillis >= 1500 || progress == 100) {
                    lastTimeMillis = vCurrentTimeMillis;
                    mHandler.progressStatus(mEntity, (int) progress);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                if (vAccessFile != null) {
                    vAccessFile.close();
                }
            } catch (IOException pE) {
                pE.printStackTrace();
            }
        }
    }

    /**
     * 检查是否支持断点续传
     *
     * @return
     */
    private boolean checkIsSupportRange() {
        try {
            mUrl = new URL(mEntity.url);
            HttpURLConnection vConnection = (HttpURLConnection) mUrl.openConnection();
            vConnection.setRequestProperty("Range", "bytes=0-");
            vConnection.setRequestMethod("GET");
            vConnection.connect();
            int code = vConnection.getResponseCode();
            Log.e("yzy", "code: " + code);
            mTotalLength = vConnection.getContentLength();
            Log.e("yzy", "totalLength: " + mTotalLength);
            if (code == 206) {
                return true;
            }
        } catch (Exception pE) {
            Log.e("yzy", "Exception: " + pE.toString());
            return false;
        }
        return false;
    }
}
