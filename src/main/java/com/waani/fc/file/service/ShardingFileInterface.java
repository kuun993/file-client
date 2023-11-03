package com.waani.fc.file.service;

import com.waani.fc.file.vo.ShardingFileVo;
import com.waani.fc.file.vo.ShardingTaskVo;
import org.springframework.core.io.InputStreamSource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author waani
 * @date 2023/11/2 15:45
 * @email kuun993@163.com
 * @description TODO
 */
public interface ShardingFileInterface {


    /**
     * 缓存分片上传任务
     * 多节点修改成 redis 或 db 等存储
     */
    Map<String, ShardingTaskVo> SHARDING_MAP = new HashMap<>();


    /**
     * 分片上传临时目录
     */
    String SHARDING_PATH = "/sharding/";


    /**
     * 开始任务
     * @param shardingTask
     */
    void startTask(ShardingTaskVo shardingTask);


    /**
     * 上传文件
     * @param shardingFile
     * @param inputStreamSource
     */
    void uploadFile(ShardingFileVo shardingFile, InputStreamSource inputStreamSource);


    /**
     * 上传文件
     * @param shardingFile
     * @param inputStream   close
     */
    void uploadFile(ShardingFileVo shardingFile, InputStream inputStream);




}
