package com.waani.fc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import sun.misc.Unsafe;

/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description SpringBootApplication
 */
@SpringBootApplication
@ConfigurationPropertiesScan({"com.waani.fc.properties"})
public class FileClientApplication {

    public static void main(String[] args) {

        SpringApplication.run(FileClientApplication.class, args);
    }

}
