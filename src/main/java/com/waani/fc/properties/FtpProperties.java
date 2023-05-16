package com.waani.fc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties("ftp")
public class FtpProperties {

    private String hostname ;

    private Integer port ;

    private String username ;

    private String password ;

    private String workingDirectory ;



}
