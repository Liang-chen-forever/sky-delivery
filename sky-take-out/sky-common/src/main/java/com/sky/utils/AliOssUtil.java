package com.sky.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传
     *
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, accessKeySecret)))
                .withPathStyleAccessEnabled(true)
                .build();

        try {
            s3Client.putObject(bucketName, objectName, new ByteArrayInputStream(bytes), null);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
        } finally {
            if (s3Client != null) {
                s3Client.shutdown();
            }
        }

        String fileUrl = endpoint + "/" + bucketName + "/" + objectName;
        log.info("文件上传到:{}", fileUrl);
        return fileUrl;
    }
}