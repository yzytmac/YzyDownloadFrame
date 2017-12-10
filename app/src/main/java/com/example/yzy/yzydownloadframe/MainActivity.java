package com.example.yzy.yzydownloadframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.yzydownloads.DataObserver;
import com.example.yzydownloads.DownLoadEntity;
import com.example.yzydownloads.DownLoadManager;

public class MainActivity extends AppCompatActivity {
    private DownLoadEntity mEntity;
    private DataObserver observer = new DataObserver() {
        @Override
        public void notifyUpdate(DownLoadEntity pEntity) {
            mEntity = pEntity;
            if(mEntity.status== DownLoadEntity.DownLoadStatus.cancle) {
                mEntity=null;
            }
            Log.e("yzy", "notifyUpdate: " + pEntity);
        }
    };
    private DownLoadManager mDownLoadManager;
    private Button mPauseBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPauseBt = (Button) findViewById(R.id.id_pause);
        mDownLoadManager = DownLoadManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDownLoadManager.addObserver(observer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDownLoadManager.deleteObserver(observer);
    }

    public void onClick(View pView) {
        if (mEntity == null) {
            mEntity = new DownLoadEntity();
            mEntity.id = "1";
            mEntity.name = "test.jpg";
            mEntity.url = "api.stay4it.com/uploads/test.jpg";
        }
        switch (pView.getId()) {
            case R.id.id_start:
                mDownLoadManager.add(mEntity);
                break;
            case R.id.id_pause:
                if (mEntity.status == DownLoadEntity.DownLoadStatus.downloading) {
                    mDownLoadManager.pause(mEntity);
                    mPauseBt.setText("恢复");
                } else if (mEntity.status == DownLoadEntity.DownLoadStatus.pause) {
                    mDownLoadManager.resume(mEntity);
                    mPauseBt.setText("暂停");
                }
                break;
            case R.id.id_cancle:
                mDownLoadManager.cancle(mEntity);
                break;
            default:
        }

    }
}
