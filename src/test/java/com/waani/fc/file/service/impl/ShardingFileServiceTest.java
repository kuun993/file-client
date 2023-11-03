package com.waani.fc.file.service.impl;

import com.waani.fc.file.service.ShardingFileInterface;
import com.waani.fc.file.vo.ShardingFileVo;
import com.waani.fc.file.vo.ShardingTaskVo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Log4j2
@SpringBootTest(properties = "spring.profiles.active=waani")
public class ShardingFileServiceTest {

    @Autowired
    private ShardingFileInterface shardingFileInterface;



    @Test
    public void uploadFile() throws Exception {
        ShardingTaskVo taskVo = new ShardingTaskVo();
        taskVo.setTaskId(UUID.randomUUID().toString());
        taskVo.setFilename("rqxs1.png");
        taskVo.setDestPath("/img/");
        taskVo.setMd5("");


        try (InputStream inputStream = Files.newInputStream(Paths.get("/Users/waani/rqxs.png"));) {

            // 10k
            int sharding = 10240;
            int available = inputStream.available();

            int number = (available / sharding) + 1;
            taskVo.setNumber(number);
            shardingFileInterface.startTask(taskVo);

            byte[] bytes = new byte[sharding];

            ShardingFileVo fileVo = new ShardingFileVo();
            fileVo.setTaskId(taskVo.getTaskId());
            int i = 1;
            while (inputStream.read(bytes) > 0) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                fileVo.setSerial(i++);
                shardingFileInterface.uploadFile(fileVo, byteArrayInputStream);
            }
        }



    }
}