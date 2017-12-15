package com.example.yzy.yzydownloadframe;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    public void setData(List<DownLoadEntity> pDatas) {
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
        final DownLoadEntity vEntity = mDatas.get(position);
        ViewHolder vHolder;
        if (pView == null) {
            pView = View.inflate(mActivity, R.layout.listview_item, null);
            TextView tv = pView.findViewById(R.id.id_status_tv);
            ImageView ivStart = pView.findViewById(R.id.id_start_icon);
            ImageView ivCancle = pView.findViewById(R.id.id_cancle_icon);
            ProgressBar bar = pView.findViewById(R.id.id_progress_bar);
            vHolder = new ViewHolder(tv, ivStart, ivCancle, bar);
            pView.setTag(vHolder);
        }
        vHolder = (ViewHolder) pView.getTag();
        vHolder.stratIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                mActivity.onStartIvClick(vEntity);
            }
        });
        vHolder.cancleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                mActivity.onCancleIvClick(vEntity);
            }
        });
        vHolder.progressBar.setProgress(vEntity.currentLength);
        if (vEntity.status == DownLoadEntity.DownLoadStatus.idle) {
            vHolder.stratIv.setImageResource(R.mipmap.start_icon);
            vHolder.cancleIv.setVisibility(View.GONE);
            vHolder.tv.setText("空闲");
        } else if (vEntity.status == DownLoadEntity.DownLoadStatus.downloading) {
            vHolder.stratIv.setImageResource(R.mipmap.pause_icon);
            vHolder.cancleIv.setVisibility(View.VISIBLE);
            vHolder.tv.setText("下载中:" + vEntity.currentLength);
        } else if (vEntity.status == DownLoadEntity.DownLoadStatus.paused) {
            vHolder.stratIv.setImageResource(R.mipmap.start_icon);
            vHolder.cancleIv.setVisibility(View.VISIBLE);
            vHolder.tv.setText("已暂停");
        } else if (vEntity.status == DownLoadEntity.DownLoadStatus.waiting) {
            vHolder.stratIv.setImageResource(R.mipmap.pause_icon);
            vHolder.cancleIv.setVisibility(View.VISIBLE);
            vHolder.tv.setText("等待中");
        } else if (vEntity.status == DownLoadEntity.DownLoadStatus.complete) {
            vHolder.stratIv.setImageResource(R.mipmap.complete_icon);
            vHolder.cancleIv.setVisibility(View.GONE);
            vHolder.tv.setText("完成");
        }

        return pView;
    }

}

class ViewHolder {
    public TextView tv;
    public ImageView stratIv, cancleIv;
    public ProgressBar progressBar;

    public ViewHolder(TextView pTv, ImageView pStratIv, ImageView pCancleIv, ProgressBar pBar) {
        tv = pTv;
        stratIv = pStratIv;
        cancleIv = pCancleIv;
        progressBar = pBar;
    }
}
