package com.waani.fc;


import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpDirEntry;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

@Log4j2
@SpringBootTest(properties = "spring.profiles.active=waani")
public class FtpTests {


    @Autowired
    private FtpClient ftpClient ;


    @SneakyThrows
    @Test
    public void test() {
        log.info("WorkingDirectory={}", ftpClient.getWorkingDirectory());


        ftpClient.changeDirectory("/video/");


        Iterator<FtpDirEntry> iterator = ftpClient.listFiles("");
        int i = 0;
        while (iterator.next() != null) {
            i++;
        }

        Assert.isTrue(i == 5, "i != 5");

    }



    @SneakyThrows
    @Test
    public void testStream() {

        ftpClient.changeDirectory("/video/");

        try (InputStream inputStream = ftpClient.getFileStream("1.mp4");
             OutputStream outputStream = Files.newOutputStream(Paths.get("/Users/waani/temp/file/xx.mp4"));) {
            IOUtils.copy(inputStream, outputStream);
        }

    }



    @SneakyThrows
    @Test
    public void testPut() {

        ftpClient.changeDirectory("/video/");

        try (InputStream inputStream = Files.newInputStream(Paths.get("/Users/waani/temp/file/3.mp4"))) {
            ftpClient.putFile("xxx1.mp4", inputStream);
        }

    }



}
