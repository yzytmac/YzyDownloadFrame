package com.example.yzydownloads;

import java.io.Serializable;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadEntity implements Serializable {
    public String id;
    public String name;
    public String url;
    public String localPath;
    public DownLoadStatus status;
    public int currentLength;
    public int totalLength;

    public DownLoadEntity(String pUrl, String pLocalPath) {
        url = pUrl;
        localPath = pLocalPath;
        String splits[] = url.split("/");
        name = splits[splits.length - 1];//文件名
        id = System.currentTimeMillis()+"";
        status = DownLoadStatus.idle;
    }

    public enum DownLoadStatus {idle, waiting, downloading, paused, cancled, complete}



    @Override
    public String toString() {
        return name + "进度：" + currentLength;
    }

    /*只要id相等，我们就认为两个对象是相等的，就是同一个对象*/
    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
