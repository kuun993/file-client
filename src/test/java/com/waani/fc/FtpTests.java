package com.waani.fc;


import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import sun.net.ftp.FtpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log4j2
@SpringBootTest(properties = "spring.profiles.active=waani")
public class FtpTests {


    @Autowired
    private FtpClient ftpClient ;


    @SneakyThrows
    @Test
    public void test() {
        log.info("WorkingDirectory={}", ftpClient.getWorkingDirectory());

        InputStream inputStream = ftpClient.getFileStream("world.txt");

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.info(line);
            }
        }

        ftpClient.putFile("world2.txt", Files.newInputStream(Paths.get("/Users/waani/world.txt")));

        Assert.state(true, "success.");
    }




}
