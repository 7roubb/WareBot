package com.osama.warebot.products;

import com.osama.warebot.exceptions.CustomExceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.net.URL;

@Slf4j
@Service
public class ImageStorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String endpoint;

    public ImageStorageService(
            @Value("${minio.endpoint:http://localhost:9000}") String endpoint,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${minio.bucket}") String bucketName) {

        this.bucketName = bucketName;
        this.endpoint = sanitizeEndpoint(endpoint);

        S3Configuration config = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(this.endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .serviceConfiguration(config)
                .region(Region.US_EAST_1)
                .build();
    }

    private String sanitizeEndpoint(String endpoint) {
        if (endpoint.contains("images.localhost")) {
            log.warn("Detected invalid MinIO endpoint '{}'. Replacing with 'http://localhost:9000'", endpoint);
            return "http://localhost:9000";
        }
        return endpoint;
    }

    public String uploadImage(String fileName, byte[] content, String contentType) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(content));
            log.info("✅ Image uploaded to MinIO: {}/{}", bucketName, fileName);
            return fileName;
        } catch (SdkClientException e) {
            log.error("❌ Failed to upload image to MinIO. Endpoint: {}, Bucket: {}", endpoint, bucketName, e);
            throw new CustomExceptions.ImageStorageException(e.getMessage());
        }
    }

    public URL getImageUrl(String fileName) {
        try {
            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build());
        } catch (SdkClientException e) {
            log.error("❌ Failed to generate URL for image. Endpoint: {}, Bucket: {}", endpoint, bucketName, e);
            throw new CustomExceptions.ImageStorageException( e.getMessage());
        }
    }
}
