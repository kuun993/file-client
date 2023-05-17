package com.waani.fc.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MinioFileModel extends FileModel {

    private String bucketName ;

    private String objectName ;

    private String contentType ;



}
