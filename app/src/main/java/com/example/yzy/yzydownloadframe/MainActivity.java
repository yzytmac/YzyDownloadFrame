package com.example.yzy.yzydownloadframe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

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
            if(i!=-1) {
                mDatas.remove(i);
                mDatas.add(i,pEntity);//替换的其实是除id以外的其他属性
                mAdapter.notifyDataSetChanged();
            }
            Log.e("yzy", "notifyUpdate: " + pEntity);
        }
    };
    private DownLoadManager mDownLoadManager;
    private ListView mLv;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLv = (ListView) findViewById(R.id.lv);
        for (int i = 0; i < 5; i++) {
            DownLoadEntity vEntity = new DownLoadEntity();
            vEntity.status = DownLoadEntity.DownLoadStatus.idle;
            vEntity.totalLength = 100;
            vEntity.currentLength = 0;
            vEntity.name = "jpg"+i;
            vEntity.id = ""+i;
            vEntity.url = "www.baidu.com"+i;
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


    public void onBtClick(DownLoadEntity pEntity) {
        switch (pEntity.status){
            case idle:
                mDownLoadManager.add(pEntity);
                break;
            case pause:
                mDownLoadManager.resume(pEntity);
                break;
            case downloading:
                mDownLoadManager.pause(pEntity);
                break;
            default:
        }
    }
}
