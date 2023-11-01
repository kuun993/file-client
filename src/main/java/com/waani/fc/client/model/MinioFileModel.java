package com.waani.fc.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description MinioFileModel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MinioFileModel extends FileModel {

    /**
     * bucketName
     */
    private String bucketName ;

    /**
     * objectName
     */
    private String objectName ;

    /**
     * contentType
     */
    private String contentType ;



}
