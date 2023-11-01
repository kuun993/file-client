package com.waani.fc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description MinioProperties
 */
@Getter
@Setter
@ConfigurationProperties("minio")
public class MinioProperties {

    private boolean enable;

    private String endpoint ;

    private String accessKey ;

    private String secretKey ;


}
