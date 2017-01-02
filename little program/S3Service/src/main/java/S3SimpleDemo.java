import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by Wan on 2016/10/4 0004.
 */
public class S3SimpleDemo {
    public final static String SUFFIX = "testFile";

    public static void main(String[] args) {
        AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
        System.out.println("parse credential profiles file successful");
        //Create S3 Client
        AmazonS3 s3client = new AmazonS3Client(credentials);
        //list buckets
        for (Bucket bucket : s3client.listBuckets()) {
            System.out.println("exist bucket:" + bucket.getName());
        }
        String bucketName = "wanpengcheng";
        String folderName = "test";
        createFolder(bucketName, folderName, s3client);

        String fileName = folderName +"/"+ SUFFIX +"/"+ "2016年01月百城样本均价.xls";
        String fileUrl = "F:\\fang_price\\2016年01月百城样本均价.xls";
        uploadFile(bucketName, fileName, fileUrl, s3client);

    }

    public static void createBucket(String bucketName, AmazonS3 s3client) {
        s3client.createBucket(bucketName);
    }

    public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                folderName + SUFFIX, emptyContent, metadata);
        // send request to S3 to create folder
        client.putObject(putObjectRequest);
    }

    public static void uploadFile(String bucketName, String fileName, String fileUrl, AmazonS3 s3client) {

        s3client.putObject(new PutObjectRequest(bucketName, fileName,
                new File(fileUrl)));
    }

    public static void deleteBucket(String bucketName, AmazonS3 s3client) {
        s3client.deleteBucket(bucketName);
    }

    public static void deletefile(String bucketName, String fileName, AmazonS3 s3client) {
        s3client.deleteObject(bucketName, fileName);
    }


}
