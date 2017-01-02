package s3Service.service;

import org.springframework.beans.factory.annotation.Autowired;
import s3Service.S3Utils;
import s3Service.UploadConfig;
import s3Service.UploadTask;
import s3Service.dao.UploadTaskDao;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Wan on 2016/10/5 0005.
 */
public class AsyncUploadTaskServiceImpl implements UploadTaskService {

    @Autowired
    private UploadTaskDao uploadTaskDao;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private S3Utils s3ClientUtil;

    private BlockingQueue<UploadTask> tasks;

    @PostConstruct
    private void init() {
        uploadTaskDao.prepareCollection();
        for (int i = 0; i < uploadConfig.getParallelism(); ++i) {
            new MongoFileUploadThread(uploadConfig, s3ClientUtil).start();
        }
        //TODO 阻塞队列保存去除大文件的内容
    }

    @Override
    public void submitTask(UploadTask task) {
        try {
            System.out.println("添加需要上传S3文件");
            uploadTaskDao.addNewS3Task(task);
        } catch (Exception e) {
            try {
                tasks.offer(task, uploadConfig.getTIMEOUT_SEC_ON_TASK_SUBMIT(), TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                System.out.println("添加需要上传S3文件任务出错" + e.getMessage());
                task.setFailMessage(e.getMessage());
                task.setStatus(UploadTask.STATUS_ENUM.FAIL);
                uploadTaskDao.update(task);
            }

        }

    }
}
