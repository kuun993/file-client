package com.waani.fc.common;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * @author chenbingkun
 * @date 2023/11/3 10:38
 * @email kuun993@163.com
 * @description TODO
 */
@EnableAsync
@Component
public class AsyncComponent {


    @Async
    public void async(AsyncExecutor asyncExecutor) {
        asyncExecutor.exec();
    }

}
