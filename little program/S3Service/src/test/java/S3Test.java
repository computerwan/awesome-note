import com.amazonaws.services.s3.AmazonS3;
import org.junit.Test;
import s3Service.S3Utils;
import s3Service.UploadTask;
import s3Service.service.AsyncUploadTaskServiceImpl;
import s3Service.service.UploadTaskService;

/**
 * Created by Wan on 2016/10/5 0005.
 */
public class S3Test {

    @Test
    public void testConnect() {
        S3Utils s3Utils = new S3Utils();
        AmazonS3 s3Client = s3Utils.connectS3();
    }

    @Test
    public void testAddNewTask() {
        UploadTask task = new UploadTask();
        UploadTaskService taskService = new AsyncUploadTaskServiceImpl();
        taskService.submitTask(task);
    }
}
