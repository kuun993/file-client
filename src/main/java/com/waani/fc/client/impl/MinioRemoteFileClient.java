package com.waani.fc.client.impl;


import com.waani.fc.client.RemoteFileClient;
import com.waani.fc.client.model.MinioFileModel;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


@RequiredArgsConstructor
public class MinioRemoteFileClient implements RemoteFileClient<MinioFileModel> {


    private final MinioClient minioClient ;

    @Override
    public void createDir(MinioFileModel model) throws Exception {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(model.getBucketName()).object(model.getDir()).stream(
                        new ByteArrayInputStream(new byte[]{}), 0, -1).build();
        minioClient.putObject(putObjectArgs);
    }

    @Override
    public void uploadByPath(MinioFileModel minioFileModel, String path) throws Exception {
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(minioFileModel.getBucketName())
                .object(minioFileModel.getObjectName())
                .filename(path)
                .build();
        minioClient.uploadObject(uploadObjectArgs) ;
    }

    @Override
    public void uploadByInputStream(MinioFileModel model, InputStream inputStream) throws Exception {
        PutObjectArgs.Builder builder = PutObjectArgs.builder()
                .bucket(model.getBucketName())
                .object(model.getObjectName())
                .stream(inputStream, inputStream.available(), -1);
        if(!StringUtils.isEmpty(model.getContentType())){
            builder.contentType(model.getContentType()) ;
        }
        PutObjectArgs putObjectArgs = builder.build();
        minioClient.putObject(putObjectArgs) ;

    }

    @Override
    public void download2File(MinioFileModel model, String path) throws Exception {
        DownloadObjectArgs downloadObjectArgs = DownloadObjectArgs.builder()
                .bucket(model.getBucketName())
                .object(model.getObjectName())
                .filename(path).build();
        minioClient.downloadObject(downloadObjectArgs);
    }

    @Override
    public InputStream download2Stream(MinioFileModel model) throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(model.getBucketName())
                .object(model.getObjectName())
                .build();
        return minioClient.getObject(getObjectArgs) ;
    }

    @Override
    public void removeFile(MinioFileModel model) throws Exception {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(model.getBucketName())
                .object(model.getObjectName())
                .build();
        minioClient.removeObject(removeObjectArgs);
    }

    @Override
    public void moveFile(MinioFileModel from, MinioFileModel to) throws Exception {
        CopyObjectArgs copyObjectArgs = CopyObjectArgs.builder()
                .bucket(to.getBucketName())
                .object(to.getObjectName())
                .source(
                        CopySource.builder()
                                .bucket(from.getBucketName())
                                .object(from.getObjectName())
                                .build())
                .build();
        minioClient.copyObject(copyObjectArgs) ;

        removeFile(from) ;
    }
}
