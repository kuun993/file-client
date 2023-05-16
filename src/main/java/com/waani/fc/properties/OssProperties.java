package com.waani.fc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties("oss")
public class OssProperties {

    private String endpoint ;

    private String accessKeyId ;

    private String accessKeySecret ;


}
