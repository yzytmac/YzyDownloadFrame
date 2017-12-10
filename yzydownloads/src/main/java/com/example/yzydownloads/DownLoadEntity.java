package com.example.yzydownloads;

import java.io.Serializable;

/**
 * Created by yzy on 2017/12/10.
 */

public class DownLoadEntity implements Serializable{
    public String id;
    public String name;
    public String url;
    public enum DownLoadStatus{waiting,downloading,pause,cancle,resume,complete}
    public DownLoadStatus status;
    public int currentLength;
    public int totalLength;

    @Override
    public String toString() {
        return "DownLoadEntity{" +
                ", status=" + status +
                ", currentLength=" + currentLength +
                ", totalLength=" + totalLength +
                '}';
    }
}
