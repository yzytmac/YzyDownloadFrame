package com.example.yzydownloads;

import java.io.Serializable;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadEntity implements Serializable {
    public String id;
    public String name;
    public String url;

    public enum DownLoadStatus {idle, waiting, downloading, paused, cancled, complete}

    public DownLoadStatus status;
    public int currentLength;
    public int totalLength;

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
