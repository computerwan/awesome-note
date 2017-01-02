package s3Service;

import org.springframework.beans.factory.annotation.Autowired;
import s3Service.dao.UploadTaskDao;

/**
 * Created by Wan on 2016/10/5 0005.
 */
public class UploadConfig {
    private String endPoint;//s3地址
    private Integer parallelism;//并发线程数
    private Integer maxPendingUploadTask;//上传工作队列的最大容量
    private final Integer MAX_UPLOAD_TIMES = 3;//上传重试次数
    private final Integer SLEEP_SEC_ON_CLIENT_FAILURE = 5;//上传失败等待时间
    protected final Integer SLEEP_SEC_IF_NO_TASK = 3;//获取不到待上传任务 线程等待时间
    private final Integer TIMEOUT_SEC_ON_TASK_SUBMIT = 3;//提交任务等待时间

    private final int mongoDocLimit = 1024 * 1024 * 16; //mongo 对单个文档大小限制为16MB

    @Autowired
    private UploadTaskDao UploadTaskDao;

    @Autowired
    private S3Utils s3ClientUtil;

    public String getEndPoint() {
        return endPoint;
    }

    public s3Service.dao.UploadTaskDao getUploadTaskDao() {
        return UploadTaskDao;
    }

    public void setUploadTaskDao(s3Service.dao.UploadTaskDao uploadTaskDao) {
        UploadTaskDao = uploadTaskDao;
    }

    public S3Utils getS3ClientUtil() {
        return s3ClientUtil;
    }

    public void setS3ClientUtil(S3Utils s3ClientUtil) {
        this.s3ClientUtil = s3ClientUtil;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Integer getParallelism() {
        return parallelism;
    }

    public void setParallelism(Integer parallelism) {
        this.parallelism = parallelism;
    }

    public Integer getMaxPendingUploadTask() {
        return maxPendingUploadTask;
    }

    public void setMaxPendingUploadTask(Integer maxPendingUploadTask) {
        this.maxPendingUploadTask = maxPendingUploadTask;
    }

    public Integer getMAX_UPLOAD_TIMES() {
        return MAX_UPLOAD_TIMES;
    }

    public Integer getSLEEP_SEC_ON_CLIENT_FAILURE() {
        return SLEEP_SEC_ON_CLIENT_FAILURE;
    }

    public Integer getSLEEP_SEC_IF_NO_TASK() {
        return SLEEP_SEC_IF_NO_TASK;
    }

    public Integer getTIMEOUT_SEC_ON_TASK_SUBMIT() {
        return TIMEOUT_SEC_ON_TASK_SUBMIT;
    }

    public int getMongoDocLimit() {
        return mongoDocLimit;
    }
}
