package com.waani.fc;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@SpringBootTest(properties = "spring.profiles.active=waani")
public class MinioTests {


    @Autowired
    private MinioClient minioClient ;


    @Test
    public void bucketExists() {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket("waani").build();
        boolean b = false;
        try {
            b = minioClient.bucketExists(bucketExistsArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assert.isTrue(b, "bucket不存在");
    }


    @Test
    public void uploadObject(){
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket("waani")
                    .object("test/male.png")
                    .filename("/Users/waani/temp/test/images/png/male.png")
                    .build();
            minioClient.uploadObject(uploadObjectArgs) ;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void putObject(){
        try {
            InputStream inputStream = Files.newInputStream(new File("/Users/waani/temp/jdk.zip").toPath()) ;
            PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket("waani").object("test/jdk.zip").stream(
                            inputStream, -1, 10485760)
                    .build();
            minioClient.putObject(putObjectArgs) ;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
