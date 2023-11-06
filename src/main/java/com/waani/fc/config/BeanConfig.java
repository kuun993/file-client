package com.waani.fc.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.waani.fc.client.RemoteFileClient;
import com.waani.fc.client.impl.FtpRemoteFileClient;
import com.waani.fc.client.impl.MinioRemoteFileClient;
import com.waani.fc.client.impl.OssRemoteFileClient;
import com.waani.fc.client.model.FtpFileModel;
import com.waani.fc.client.model.MinioFileModel;
import com.waani.fc.client.model.OssFileModel;
import com.waani.fc.properties.FtpProperties;
import com.waani.fc.properties.MinioProperties;
import com.waani.fc.properties.OssProperties;
import io.minio.MinioClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;
import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description 配置 Bean
 */
@Log4j2
public class BeanConfig {



    @Bean(destroyMethod="shutdown")
    @ConditionalOnMissingBean(value = OSS.class)
    @ConditionalOnProperty(prefix = "oss", name = "enable", havingValue = "true")
    public OSS ossClient(OssProperties ossProperties){
        log.info("creating ossClient ...");
        OSS oss = new OSSClientBuilder()
                .build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        log.info("create ossClient success.");
        return oss ;
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(prefix = "ftp", name = "enable", havingValue = "true")
    @ConditionalOnMissingBean(value = FtpClient.class)
    public FtpClient ftpClient(FtpProperties ftpProperties) throws FtpProtocolException, IOException {
        log.info("creating ftpClient ...");
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ftpProperties.getHostname(), ftpProperties.getPort()) ;
        FtpClient ftpClient = FtpClient.create(inetSocketAddress) ;
        ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword().toCharArray()) ;
        ftpClient.changeDirectory(ftpProperties.getWorkingDirectory()) ;
        log.info("create ftpClient success.");
        return ftpClient ;
    }


    @Bean
    @ConditionalOnProperty(prefix = "minio", name = "enable", havingValue = "true")
    @ConditionalOnMissingBean(value = MinioClient.class)
    public MinioClient minioClient(MinioProperties minioProperties){
        log.info("creating minioClient ...");
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(minioProperties.getEndpoint())
                        .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                        .build();
        log.info("create minioClient success.");
        return minioClient ;
    }




    // ~ fileClient



    @Bean(value = "ossRemoteFileClient")
    @ConditionalOnProperty(prefix = "oss", name = "enable", havingValue = "true")
    @ConditionalOnMissingBean(value = OssRemoteFileClient.class)
    @ConditionalOnBean(value = OSS.class)
    public RemoteFileClient<OssFileModel> ossRemoteFileClient(OSS ossClient) {
        log.info("creating ossRemoteFileClient...");
        return new OssRemoteFileClient(ossClient) ;
    }

    @Bean(value = "ftpRemoteFileClient")
    @ConditionalOnProperty(prefix = "ftp", name = "enable", havingValue = "true")
    @ConditionalOnMissingBean(value = FtpRemoteFileClient.class)
    @ConditionalOnBean(value = FtpClient.class)
    public RemoteFileClient<FtpFileModel> ftpRemoteFileClient(FtpClient ftpClient) {
        log.info("creating ftpRemoteFileClient...");
        return new FtpRemoteFileClient(ftpClient) ;
    }


    @Bean(value = "minioRemoteFileClient")
    @ConditionalOnProperty(prefix = "minio", name = "enable", havingValue = "true")
    @ConditionalOnMissingBean(value = MinioRemoteFileClient.class)
    @ConditionalOnBean(value = MinioClient.class)
    public RemoteFileClient<MinioFileModel> minioRemoteFileClient(MinioClient minioClient) {
        log.info("creating minioRemoteFileClient...");
        return new MinioRemoteFileClient(minioClient) ;
    }


}
