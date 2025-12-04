package org.example.car_dealership.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Service
@Slf4j
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3ServiceImpl(S3Client s3Client, S3Presigner s3Presigner,
                         @Value("${aws.s3.bucket-name}")
                         String bucketName) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }


    @Override
    public String uploadMultipartFile(String key, MultipartFile file) {
        try(InputStream inputStream = file.getInputStream()) {
            String contentType = file.getContentType();
            if(contentType == null){
                contentType = "application/octet-stream";
            }

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            log.info("Successfully uploaded file to s3 bucket: {}", key);
            return key;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generatePresignedUrl(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            Duration expiration = Duration.ofHours(1);

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(expiration)
                    .getObjectRequest(getObjectRequest)
                    .build();

            String url = s3Presigner.presignGetObject(presignRequest).url().toString();
            log.debug("Generated presigned URL for key: {}", key);
            return url;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
