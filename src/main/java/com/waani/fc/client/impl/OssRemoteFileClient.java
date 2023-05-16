package com.waani.fc.client.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.waani.fc.client.RemoteFileClient;
import com.waani.fc.client.model.OssFileModel;
import lombok.RequiredArgsConstructor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


@RequiredArgsConstructor
public class OssRemoteFileClient implements RemoteFileClient<OssFileModel> {


    private final OSS ossClient ;

    @Override
    public void createDir(OssFileModel model) throws OSSException, ClientException {
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(model.getBucketName(), model.getDir() + "/", new ByteArrayInputStream("".getBytes()));
        ossClient.putObject(putObjectRequest);
    }

    @Override
    public void uploadByPath(OssFileModel model, String path) throws OSSException, ClientException, IOException {
        uploadByInputStream(model, Files.newInputStream(Paths.get(path)));
    }

    @Override
    public void uploadByInputStream(OssFileModel model, InputStream inputStream) throws OSSException, ClientException {
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(model.getBucketName(), model.getObjectName(), inputStream) ;
        ossClient.putObject(putObjectRequest) ;
    }

    @Override
    public void download2File(OssFileModel model, String path) throws OSSException, ClientException {
        ossClient.getObject(new GetObjectRequest(model.getBucketName(), model.getObjectName()), new File(path));
    }

    @Override
    public InputStream download2Stream(OssFileModel model) throws OSSException, ClientException {
        OSSObject ossObject = ossClient.getObject(model.getBucketName(), model.getObjectName());
        return ossObject.getObjectContent() ;
    }

    @Override
    public void removeFile(OssFileModel model) throws OSSException, ClientException {
        ossClient.deleteObject(model.getBucketName(), model.getObjectName());
    }

    @Override
    public void moveFile(OssFileModel from, OssFileModel to) throws OSSException, ClientException {
        CopyObjectRequest copyObjectRequest =
                new CopyObjectRequest(from.getBucketName(), from.getObjectName(), to.getBucketName(), to.getObjectName()) ;
        ossClient.copyObject(copyObjectRequest) ;
        removeFile(from);
    }
}
