package com.waani.fc;


import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpDirEntry;
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


        ftpClient.changeDirectory("/hello/");


        Iterator<FtpDirEntry> iterator = ftpClient.listFiles("");
        int i = 0;
        while (iterator.next() != null) {
            i++;
        }

        Assert.isTrue(i == 5, "i != 5");

    }




}
