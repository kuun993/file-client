package com.waani.fc.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description OssFileModel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OssFileModel extends FileModel {

    /**
     * bucketName
     */
    private String bucketName ;


    /**
     * objectName
     */
    private String objectName ;


}
