package s3Service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;

/**
 * Created by Wan on 2016/10/5 0005.
 */
public class S3Utils {
    public AmazonS3 connectS3() {
        AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        AmazonS3 s3client = new AmazonS3Client(credentials);
        s3client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
        System.out.println("Link successful");
        return s3client;
    }
}
