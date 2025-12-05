package site.treetory.global.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3Client amazonS3Client() {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setMaxConnections(100);
        clientConfig.setConnectionTimeout(60_000);
        clientConfig.setSocketTimeout(60_000);
        clientConfig.setProtocol(Protocol.HTTPS);

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withClientConfiguration(clientConfig)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
