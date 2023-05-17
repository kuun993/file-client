package com.waani.fc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties("minio")
public class MinioProperties {

    private String endpoint ;

    private String accessKey ;

    private String secretKey ;


}
