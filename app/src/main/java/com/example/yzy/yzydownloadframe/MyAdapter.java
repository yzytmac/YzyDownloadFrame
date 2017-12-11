package com.example.yzy.yzydownloadframe;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.yzydownloads.DownLoadEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 17-12-11.
 */

public class MyAdapter extends BaseAdapter {

    private final MainActivity mActivity;
    private List<DownLoadEntity> mDatas = new ArrayList<>();

    public MyAdapter(MainActivity context) {
        mActivity = context;
    }

    public void setData(List<DownLoadEntity> pDatas){
        mDatas = pDatas;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int pI) {
        return mDatas.get(pI);
    }

    @Override
    public long getItemId(int pI) {
        return pI;
    }

    @Override
    public View getView(int position, View pView, ViewGroup pViewGroup) {
        ViewHolder vHolder;
        if(pView == null) {
            pView = View.inflate(mActivity, R.layout.listview_item, null);
            TextView tv = pView.findViewById(R.id.item_tv);
            Button bt = pView.findViewById(R.id.item_bt);
            vHolder = new ViewHolder(tv,bt);
            pView.setTag(vHolder);
        }
        vHolder = (ViewHolder) pView.getTag();
        final DownLoadEntity vEntity = mDatas.get(position);
        vHolder.tv.setText("进度:"+vEntity.currentLength+"/"+ vEntity.totalLength);
        if(vEntity.status == DownLoadEntity.DownLoadStatus.idle) {
            vHolder.bt.setText("开始");
        }else if(vEntity.status == DownLoadEntity.DownLoadStatus.downloading) {
            vHolder.bt.setText("暂停");
        }else if(vEntity.status == DownLoadEntity.DownLoadStatus.pause) {
            vHolder.bt.setText("恢复");
        }else if(vEntity.status == DownLoadEntity.DownLoadStatus.waiting) {
            vHolder.bt.setText("等待");
        }else if(vEntity.status == DownLoadEntity.DownLoadStatus.complete) {
            vHolder.bt.setText("完成");
        }
        vHolder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                mActivity.onBtClick(vEntity);
            }
        });
        return pView;
    }

}

class ViewHolder{
    public TextView tv;
    public Button bt;
    public ViewHolder(TextView pTv, Button pBt) {
        this.tv = pTv;
        this.bt = pBt;
    }
}
