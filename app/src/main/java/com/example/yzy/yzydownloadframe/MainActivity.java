package com.example.yzy.yzydownloadframe;

import android.app.ActionBar;
import android.os.Bundle;
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
    private DownLoadEntity mEntity;
    private ArrayList<DownLoadEntity> mDatas = new ArrayList<>();
    private DataObserver observer = new DataObserver() {
        @Override
        public void notifyUpdate(DownLoadEntity pEntity) {
            mEntity = pEntity;
            int i = mDatas.indexOf(pEntity);//重写了equals方法，id相同就认为是同一个对象，但是其他属性不同
            if (i != -1) {
                mDatas.remove(i);
                mDatas.add(i, pEntity);//替换的其实是除id以外的其他属性
                mAdapter.notifyDataSetChanged();
            }
            Log.e("yzy", "notifyUpdate: " + pEntity);
        }
    };
    private DownLoadManager mDownLoadManager;
    private ListView mLv;
    private MyAdapter mAdapter;
    private Button mPauseAllBt;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLv = (ListView) findViewById(R.id.lv);
        mPauseAllBt = (Button) findViewById(R.id.pause_all_bt);


        for (int i = 0; i < 10; i++) {
            DownLoadEntity vEntity = new DownLoadEntity();
            vEntity.status = DownLoadEntity.DownLoadStatus.idle;
            vEntity.totalLength = 100;
            vEntity.currentLength = 0;
            vEntity.name = "jpg" + i;
            vEntity.id = "" + i;
            vEntity.url = "www.baidu.com" + i;
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
                if (i++ % 2 == 0) {
                    mDownLoadManager.pauseAll();
                    mPauseAllBt.setText("恢复全部");
                }else {
                    mDownLoadManager.resumeAll();
                    mPauseAllBt.setText("暂停全部");
                }
                break;
            case R.id.cancle_all_bt:
                mDownLoadManager.resumeAll();
                break;
            default:
        }
    }


    public void onStartIvClick(DownLoadEntity pEntity) {
        switch (pEntity.status) {
            case idle:
                mDownLoadManager.add(pEntity);
                break;
            case pause:
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

    }
}
