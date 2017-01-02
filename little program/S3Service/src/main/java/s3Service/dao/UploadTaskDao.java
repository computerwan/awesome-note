package s3Service.dao;

import s3Service.UploadTask;

/**
 * Created by Wan on 2016/10/5 0005.
 */

/**
 * addNewS3Task:新增上传任务
 * fetchS3Task：获取需要上传的s3文件(包含重传)
 * update：更新需要上传的文件
 * finishTask：完成后删除
 */
public interface UploadTaskDao {
    void prepareCollection();

    void addNewS3Task(UploadTask task);

    UploadTask fetchS3Task();

    void update(UploadTask task);

    void finishTask(UploadTask task);


}
