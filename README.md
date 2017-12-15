# YzyDownloadFrame
**手写Java/Android多任务下载框架-不依赖任何第三封库**  
使用方法：  
1、拷贝yzydownload作为一个moudel依赖  
2、整个下载过程秩序操作DownLoadManger这一个类  
        
        getInstance(Context pContext)获取DownLoadManger实例  
        downLoad(DownLoadEntity pEntity)下载  
        pause(DownLoadEntity pEntity)暂停  
        resume(DownLoadEntity pEntity)恢复  
        cancle(DownLoadEntity pEntity)取消  
        pauseAll()暂停全部  
        resumeAll()恢复全部  
        cancleAll()取消全部  
        addObserver(DataObserver pObserver)添加监听  
        deleteObserver(DataObserver pObserver)移除监听  
3、DownLoadEntity封装了下载的信息，如：url、name等信息  
4、DataObserver中会回调下载情况，将DownLoadEntity又返回，回调在主线程中回调，可以直接刷新ui  
5、权限问题，需要申请网络权限、读写sd卡权限。如果在6.0及以上系统中使用需要动态申请读写sd卡的权限  
6、有任何疑问欢迎发邮件到www.yzytmac.163.com 欢迎提交代码  

还有一些小细节有待完善。未完待续。。。。。。
