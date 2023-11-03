package com.waani.fc.file.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author waani
 * @date 2023/11/2 10:30
 * @email kuun993@163.com
 * @description 文件上传 分片文件实体类
 */
@Getter
@Setter
@ToString
public class ShardingFileVo implements Serializable {


    private static final long serialVersionUID = -7317434900036752660L;


    /**
     * 任务ID
     * 用于关联分片
     */
    private String taskId;



    /**
     * 分片序号
     * from 0
     */
    private int serial;



}
