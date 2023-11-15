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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description OssRemoteFileClient
 */
@RequiredArgsConstructor
public class OssRemoteFileClient implements RemoteFileClient<OssFileModel> {


    private final OSS ossClient ;

    @Override
    public void createDirectory(OssFileModel model) throws OSSException, ClientException {
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(model.getBucketName(), model.getRemoteDirectory() + "/", new ByteArrayInputStream("".getBytes()));
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


    @Override
    public void removeDirectoryAndFiles(OssFileModel ossFileModel) {
        String bucketName = ossFileModel.getBucketName();
        String remoteDirectory = ossFileModel.getRemoteDirectory();
        String nextMarker = null;
        ObjectListing objectListing = null;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName)
                    .withPrefix(remoteDirectory)
                    .withMarker(nextMarker);
            objectListing = ossClient.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().size() > 0) {

                List<String> keys = objectListing.getObjectSummaries().stream().map(OSSObjectSummary::getKey).collect(Collectors.toList());

                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keys).withEncodingType("url");
                DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(deleteObjectsRequest);
                deleteObjectsResult.getDeletedObjects();
            }
            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
    }
}
