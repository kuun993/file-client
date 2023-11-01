package com.waani.fc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description FtpProperties
 */
@Getter
@Setter
@ConfigurationProperties("ftp")
public class FtpProperties {

    private boolean enable;

    private String hostname ;

    private Integer port ;

    private String username ;

    private String password ;

    private String workingDirectory ;



}
