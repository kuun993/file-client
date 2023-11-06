package com.waani.fc;


import com.waani.fc.client.RemoteFileClient;
import com.waani.fc.client.impl.FtpRemoteFileClient;
import com.waani.fc.client.model.FtpFileModel;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private FtpClient ftpClient;

    @Qualifier("ftpRemoteFileClient")
    @Autowired
    private RemoteFileClient<FtpFileModel> ftpRemoteFileClient;


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

        ftpClient.changeDirectory("/h/");

        try (InputStream inputStream = Files.newInputStream(Paths.get("/Users/waani/temp/file/1.mp4"))) {
            ftpClient.putFile("1.mp4", inputStream);
        }

    }


    @SneakyThrows
    @Test
    public void mkDir() {
        FtpFileModel fileModel = new FtpFileModel();
        fileModel.setRemoteDirectory("/h/w/waani/");
        ftpRemoteFileClient.createDirectory(fileModel);
    }


    @SneakyThrows
    @Test
    public void removeDirectoryAndFiles() {
        FtpFileModel fileModel = new FtpFileModel();
        fileModel.setRemoteDirectory("/h");
        ftpRemoteFileClient.removeDirectoryAndFiles(fileModel);
    }



}
