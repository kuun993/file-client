package com.waani.fc.file.service.impl;

import com.waani.fc.common.AsyncComponent;
import com.waani.fc.file.service.ShardingFileInterface;
import com.waani.fc.file.vo.ShardingFileVo;
import com.waani.fc.file.vo.ShardingTaskVo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import sun.net.ftp.FtpDirEntry;
import sun.net.ftp.FtpClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

/**
 * @author waani
 * @date 2023/11/2 15:45
 * @email kuun993@163.com
 * @description TODO
 */
@Log4j2
@RequiredArgsConstructor
@Service
public class ShardingFileService implements ShardingFileInterface {


    private final FtpClient ftpClient ;


    private final AsyncComponent asyncComponent;


    @SneakyThrows
    @Override
    public void startTask(ShardingTaskVo shardingTask) {
        // 每个任务创建一个目录
        changeDirectory(SHARDING_PATH + shardingTask.getTaskId());

        // 缓存分片任务
        SHARDING_MAP.put(shardingTask.getTaskId(), shardingTask);

    }

    @SneakyThrows
    @Override
    public void uploadFile(ShardingFileVo shardingFile, InputStreamSource inputStreamSource) {
        try (InputStream inputStream = inputStreamSource.getInputStream()) {
            uploadFile(shardingFile, inputStream);
        }
    }


    @SneakyThrows
    @Override
    public void uploadFile(ShardingFileVo shardingFile, InputStream inputStream) {
        changeDirectory(SHARDING_PATH + shardingFile.getTaskId());

        // 写文件
        ftpClient.putFile(String.valueOf(shardingFile.getSerial()), inputStream);

        // 判断当前文件是否为最后一块
        if (!lastOne(shardingFile.getTaskId())) {
            return;
        }

        genFileAndUpload(shardingFile);
    }

    /**
     * 是否缺少最后一块
     * @param taskId
     * @return
     */
    @SneakyThrows
    private boolean lastOne(String taskId) {
        int len = 0;
        Iterator<FtpDirEntry> iterator = ftpClient.listFiles("");
        while (iterator.hasNext() && iterator.next() != null) {
            len++;
        }
        ShardingTaskVo shardingTask = SHARDING_MAP.get(taskId);
        if (null == shardingTask) {
            return false;
        }
        return shardingTask.getNumber() == len;
    }



    @SneakyThrows
    private void genFileAndUpload(ShardingFileVo shardingFile) {
        ShardingTaskVo shardingTask = SHARDING_MAP.get(shardingFile.getTaskId());

        int number = shardingTask.getNumber();

        String remoteFile = shardingTask.getDestPath() + shardingTask.getFilename();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            for (int i = 1; i <= number; i++) {
                try (InputStream inputStream = ftpClient.getFileStream(String.valueOf(i));) {
                    write(outputStream, inputStream);
                    log.info("write: {}", i);
                }
            }
            ftpClient.putFile(remoteFile, new ByteArrayInputStream(outputStream.toByteArray()));
        } finally {
            // 删除文件块
            removeDirectoryAndFiles(SHARDING_PATH + shardingTask.getTaskId());
        }

    }




    /**
     * 写到输出流
     * @param outputStream
     * @param inputStream
     */
    @SneakyThrows
    private void write(OutputStream outputStream, InputStream inputStream) {
        byte[] bytes = new byte[1024];
        while (inputStream.read(bytes) > 0) {
            outputStream.write(bytes);
        }
    }


    /**
     * 合并输入流
     * @param s1
     * @param s2
     * @return
     */
    private InputStream merge(InputStream s1, InputStream s2) {
        return new SequenceInputStream(s1, s2);
    }


    /**
     * changeDirectory
     * @param remoteDirectory
     */
    @SneakyThrows
    private void changeDirectory(String remoteDirectory) {
        try {
            ftpClient.changeDirectory(remoteDirectory);
        } catch (Exception e) {
            ftpClient.makeDirectory(remoteDirectory);
            ftpClient.changeDirectory(remoteDirectory);
        }
    }


    @SneakyThrows
    private void removeDirectoryAndFiles(String remoteDirectory) {
        changeDirectory(remoteDirectory);
        Iterator<FtpDirEntry> iterator = ftpClient.listFiles("");
        while (iterator.hasNext()) {
            FtpDirEntry next = iterator.next();
            ftpClient.deleteFile(next.getName());
        }
        ftpClient.removeDirectory(remoteDirectory);
    }


}
