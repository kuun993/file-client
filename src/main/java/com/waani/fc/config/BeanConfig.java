package com.waani.fc.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.waani.fc.properties.FtpProperties;
import com.waani.fc.properties.OssProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.IOException;
import java.net.InetSocketAddress;

@Log4j2
@RequiredArgsConstructor
@ConfigurationPropertiesScan({"com.waani.fc.properties"})
public class BeanConfig {

    private final OssProperties ossProperties ;

    private final FtpProperties ftpProperties ;


    @Bean(destroyMethod = "close")
    public FtpClient ftpClient() throws FtpProtocolException, IOException {
        log.info("creating ftpClient ...");
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ftpProperties.getHostname(), ftpProperties.getPort()) ;
        FtpClient ftpClient = FtpClient.create(inetSocketAddress) ;
        ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword().toCharArray()) ;
        ftpClient.changeDirectory(ftpProperties.getWorkingDirectory()) ;
        log.info("create ftpClient success.");
        return ftpClient ;
    }


    @Bean(destroyMethod="shutdown")
    public OSS ossClient(){
        log.info("creating ossClient ...");
        OSS oss = new OSSClientBuilder()
                .build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        log.info("create ossClient success.");
        return oss ;
    }

}
