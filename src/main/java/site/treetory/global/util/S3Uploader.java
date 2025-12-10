package site.treetory.global.util;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import site.treetory.global.exception.CustomException;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import static site.treetory.global.statuscode.ErrorCode.*;
import static site.treetory.global.util.DateFormatter.convertToTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {

    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp");

    private final S3Client amazonS3Client;
    private final ProxyManager<String> proxyManager;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String s3bucket;

    @Value("${spring.cloud.aws.s3.url}")
    private String bucketUrl;

    public String upload(Long memberId, MultipartFile multipartFile, String dirName) {

        String key = "rate_limit:" + memberId;

        Bucket bucket = proxyManager.builder().build(key, getConfigSupplier());

        if (!bucket.tryConsume(1)) {
            throw new CustomException(TOO_MANY_REQUESTS);
        }

        if (multipartFile.isEmpty() || Objects.isNull(multipartFile.getOriginalFilename())) {
            throw new CustomException(BAD_REQUEST);
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String extension = getExtension(originalFilename);

        validateImageFileExtension(extension);

        String s3FileName = convertS3Name(dirName, extension);
        String contentType = determineContentType(extension);

        return uploadFile(multipartFile, s3FileName, contentType);
    }

    private String getExtension(String filename) {

        int lastDotIndex = filename.lastIndexOf(".");

        if (lastDotIndex == -1) {
            throw new CustomException(BAD_REQUEST);
        }

        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private String determineContentType(String extension) {

        if (extension.equals("jpg")) {
            return "image/jpeg";
        }

        return "image/" + extension;
    }

    private String convertS3Name(String dirName, String extension) {
        return String.format("%s/%s:upload_%s.%s", dirName, UUID.randomUUID(), convertToTime(LocalDateTime.now()), extension);
    }

    private String uploadFile(MultipartFile multipartFile, String s3FileName, String contentType) {

        try (InputStream inputStream = multipartFile.getInputStream()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3bucket)
                    .key(s3FileName)
                    .contentType(contentType)
                    .contentLength(multipartFile.getSize())
                    .build();

            RequestBody requestBody = RequestBody.fromInputStream(inputStream, multipartFile.getSize());
            amazonS3Client.putObject(putObjectRequest, requestBody);
        } catch (Exception e) {
            log.error("S3 Upload Fail: {}", e.getMessage());
            throw new CustomException(IMAGE_UPLOAD_FAIL);
        }

        return generateS3(s3FileName);
    }

    private String generateS3(String s3FileName) {
        return bucketUrl + s3FileName;
    }

    private void validateImageFileExtension(String extension) {

        if (!SUPPORTED_EXTENSIONS.contains(extension)) {
            throw new CustomException(BAD_REQUEST);
        }
    }

    private Supplier<BucketConfiguration> getConfigSupplier() {

        return () -> BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(50).refillIntervally(2, Duration.ofHours(1L)))
                .build();
    }
}