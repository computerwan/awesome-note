package s3Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Wan on 2016/10/5 0005.
 */
public class UploadTask {
    public enum STATUS_ENUM {
        SUCCEED,
        FAIL,
        DEFAULT,//尚未执行
        PROCESSING, //正在执行
    }

    private String bucket;
    private String fileName;
    private String fileUrl;
    private STATUS_ENUM status;
    private String failMessage;//错误消息
    private Date submitTime; //提交时间
    public AtomicLong tryCounter = new AtomicLong(0L);//已重试次数

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getBucket() {
        return bucket;
    }

    public UploadTask setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public UploadTask setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public UploadTask setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public STATUS_ENUM getStatus() {
        return status;
    }

    public void setStatus(STATUS_ENUM status) {
        this.status = status;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }
}
