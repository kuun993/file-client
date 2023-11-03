package com.waani.fc.file.service.impl;

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



    @SneakyThrows
    @Override
    public void startTask(ShardingTaskVo shardingTask) {
        changeDirectory(SHARDING_PATH);

        // 每个任务创建一个目录
        ftpClient.makeDirectory(shardingTask.getTaskId());

        // 多节点修改成 redis、zk和db 等存储
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
        // 判断当前文件是否为最后一块
        if (lastOne(shardingFile.getTaskId())) {
            genFile(shardingFile, inputStream);
            return;
        }
        // 写文件
        ftpClient.putFile(String.valueOf(shardingFile.getSerial()), inputStream);
    }

    /**
     * 是否缺少最后一块
     * @param taskId
     * @return
     */
    @SneakyThrows
    private boolean lastOne(String taskId) {
        int len = 1;
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
    private void genFile(ShardingFileVo shardingFile, InputStream is) {
        ShardingTaskVo shardingTask = SHARDING_MAP.get(shardingFile.getTaskId());
        InputStream all = null;
        try {
            int number = shardingTask.getNumber();
            for (int i = 1; i <= number; i++) {
                InputStream inputStream = (shardingFile.getSerial() == i)? is: ftpClient.getFileStream(String.valueOf(i));
                if (all == null) {
                    all = inputStream;
                    continue;
                }
                all = merge(all, inputStream);
            }

            // 上传文件
            changeDirectory(shardingTask.getDestPath());
            ftpClient.putFile(shardingTask.getFilename(), all);

            // 删除文件块
            removeDirectoryAndFiles(SHARDING_PATH + shardingFile.getTaskId());

        } finally {
            IOUtils.close(all);
        }
    }


    /**
     * 写到输入流
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
