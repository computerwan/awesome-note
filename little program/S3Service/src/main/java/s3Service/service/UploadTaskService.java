package s3Service.service;

import s3Service.UploadTask;

/**
 * Created by Wan on 2016/10/5 0005.
 */
public interface UploadTaskService {
    void submitTask(UploadTask task);//添加任务
}
