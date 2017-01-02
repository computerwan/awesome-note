package s3Service.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import s3Service.S3Utils;
import s3Service.UploadConfig;
import s3Service.UploadTask;

import java.io.File;

/**
 * Created by Wan on 2016/10/5 0005.
 */
public abstract class FileUploadThreadTemplate extends Thread {

    protected UploadConfig uploadConfig;
    private S3Utils s3ClientUtil;

    public FileUploadThreadTemplate(UploadConfig config, S3Utils s3ClientUtil, String threadName) {
        super(threadName);
        this.uploadConfig = config;
        this.s3ClientUtil = s3ClientUtil;
    }

    protected abstract UploadTask fetchTask();//获取任务

    protected abstract void updateTask(UploadTask uploadTask);//更新任务状态


    @Override
    public void run() {
        System.out.println("启动s3上传线程");
        UploadTask task = null;
        while (true) {
            try {
                boolean upLoadResult = false;//判断任务是否成功
                while ((task = fetchTask()) == null) {
                    Thread.sleep(uploadConfig.getSLEEP_SEC_IF_NO_TASK() * 1000); //如果当前没有任务，线程休眠时间
                }
                task.tryCounter.getAndIncrement();
                upLoadResult = upLoadFile(task, s3ClientUtil.connectS3());//上传文件

                if (upLoadResult) {
                    task.setStatus(UploadTask.STATUS_ENUM.SUCCEED);
                    task.setFailMessage("");
                    updateTask(task);
                    System.out.println("上传任务成功");

                } else if (task.tryCounter.get() < uploadConfig.getMAX_UPLOAD_TIMES()) {
                    Thread.sleep(uploadConfig.getSLEEP_SEC_ON_CLIENT_FAILURE() * 1000);//上传失败等待时间
                    task.setStatus(UploadTask.STATUS_ENUM.DEFAULT);
                    updateTask(task);
                    System.out.println("任务" + task + "已重置");
                } else {
                    task.setStatus(UploadTask.STATUS_ENUM.FAIL);
                    task.setFailMessage("上传任务失败");
                    updateTask(task);
                    System.out.println("上传任务失败");
                }
            } catch (Exception e) {
                System.out.println("上传时出现错误");
            }
        }
    }

    private boolean upLoadFile(UploadTask task, AmazonS3 s3client) {
        String bucketName = task.getBucket();
        String fileName = task.getFileName();
        String fileUrl = task.getFileUrl();
        Long retryTimes = task.tryCounter.get();
        boolean uploadResult;
        try {
            System.out.println("开始上传文件");
            s3client.putObject(new PutObjectRequest(bucketName, fileName, new File(fileUrl)));
            uploadResult = true;
        } catch (Exception e) {
            uploadResult = false;
            System.out.println("第" + retryTimes + "次上传文件失败");
        }
        return uploadResult;
    }
}
