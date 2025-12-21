package site.treetory.global.config;

import io.awspring.cloud.autoconfigure.s3.S3ClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.http.apache.ApacheHttpClient;

import java.time.Duration;

@Configuration
public class S3Config {

    @Bean
    public S3ClientCustomizer amazonS3Client() {
        return builder -> {
            ApacheHttpClient.Builder httpClientBuilder = ApacheHttpClient.builder()
                    .maxConnections(100)
                    .connectionTimeout(Duration.ofMillis(60_000))
                    .socketTimeout(Duration.ofMillis(60_000));

            builder.httpClientBuilder(httpClientBuilder);
        };
    }
}
