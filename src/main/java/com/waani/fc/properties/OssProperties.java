package com.waani.fc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description OssProperties
 */
@Getter
@Setter
@ConfigurationProperties("oss")
public class OssProperties {

    private String endpoint ;

    private String accessKeyId ;

    private String accessKeySecret ;


}
