package com.example.yzy.yzydownloadframe;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yzydownloads.DataObserver;
import com.example.yzydownloads.DownLoadEntity;
import com.example.yzydownloads.DownLoadManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<DownLoadEntity> mDatas = new ArrayList<>();
    private DownLoadManager mDownLoadManager;
    private ListView mLv;
    private MyAdapter mAdapter;
    private Button mPauseAllBt;
    private int flag = 0;

    /**
     * 监听器，接受下载层的回调
     */
    private DataObserver observer = new DataObserver() {
        @Override
        public void notifyUpdate(DownLoadEntity pEntity) {
            int index = mDatas.indexOf(pEntity);//重写了equals方法，id相同就认为是同一个对象，但是其他属性不同
            if (index != -1) {
                mDatas.remove(index);
                mDatas.add(index, pEntity);//替换的其实是除id以外的其他属性
                mAdapter.notifyDataSetChanged();
            }
            Log.e("yzy", "notifyUpdate: " + pEntity);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLv = (ListView) findViewById(R.id.lv);
        mPauseAllBt = (Button) findViewById(R.id.pause_all_bt);
        String localPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/00";

        for (int i = 0; i < 100; i++) {
            DownLoadEntity vEntity = new DownLoadEntity("http://ofbrh1334.bkt.clouddn.com/ddr.mp4",localPath);
            vEntity.name = "ddr"+i+".mp4";//此处只用了一个视频源，所以改一下文件的名字，不然所有的下载都是一个文件。
            mDatas.add(vEntity);
        }
        mAdapter = new MyAdapter(this);
        mLv.setAdapter(mAdapter);
        mAdapter.setData(mDatas);
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
        switch (pView.getId()) {
            case R.id.pause_all_bt:
                if (flag++ % 2 == 0) {
                    mDownLoadManager.pauseAll();
                    mPauseAllBt.setText("恢复全部");
                }else {
                    mDownLoadManager.resumeAll();
                    mPauseAllBt.setText("暂停全部");
                }
                break;
            case R.id.cancle_all_bt:
                mDownLoadManager.cancleAll();
                break;
            default:
        }
    }


    public void onStartIvClick(DownLoadEntity pEntity) {
        switch (pEntity.status) {
            case idle:
                mDownLoadManager.add(pEntity);
                break;
            case paused:
                mDownLoadManager.resume(pEntity);
                break;
            case downloading:
            case waiting:
                mDownLoadManager.pause(pEntity);
                break;
            case complete:
                Toast.makeText(MainActivity.this, "已经完成啦，别点啦", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    public void onCancleIvClick(DownLoadEntity pEntity) {
        mDownLoadManager.cancle(pEntity);
    }
}
