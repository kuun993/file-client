package com.waani.fc.file.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author waani
 * @date 2023/11/2 10:30
 * @email kuun993@163.com
 * @description 文件上传 分片任务实体类
 */
@Getter
@Setter
@ToString
public class ShardingTaskVo implements Serializable {


    private static final long serialVersionUID = -7317434900036752660L;


    /**
     * 任务ID
     * 用于关联分片
     */
    private String taskId;


    /**
     * 文件md5
     * 用于判断文件是否存在、判断文件是否完整等
     */
    private String md5;


    /**
     * 目的路径
     */
    private String destPath;

    /**
     * 文件名
     */
    private String filename;


    /**
     * 分片数量
     */
    private int number;



}
