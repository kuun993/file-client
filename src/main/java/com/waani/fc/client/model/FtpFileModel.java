package com.waani.fc.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description FtpFileModel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FtpFileModel extends FileModel {


    /**
     * 文件名
     */
    private String remoteName ;



}
