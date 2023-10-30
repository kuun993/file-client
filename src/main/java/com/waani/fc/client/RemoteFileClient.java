package com.waani.fc.client;

import com.waani.fc.client.model.FileModel;
import java.io.InputStream;

public interface RemoteFileClient<T extends FileModel> {

    /**
     * 创建目录
     * @param t
     * @throws Exception
     */
    void createDir(T t) throws Exception;

    /**
     * 根据文件路径上传文件
     * @param t
     * @param path
     * @throws Exception
     */
    void uploadByPath(T t, String path) throws Exception;

    /**
     * 根据文件流上传文件
     * @param t
     * @param inputStream
     * @throws Exception
     */
    void uploadByInputStream(T t, InputStream inputStream) throws Exception;

    /**
     * 下载文件到本地
     * @param t
     * @param path
     * @throws Exception
     */
    void download2File(T t, String path) throws Exception;

    /**
     * 读取文件流
     * @param t
     * @return
     * @throws Exception
     */
    InputStream download2Stream(T t) throws Exception;

    /**
     * 删除文件
     * @param t
     * @throws Exception
     */
    void removeFile(T t) throws Exception;

    /**
     * 移动文件
     * @param f
     * @param t
     * @throws Exception
     */
    void moveFile(T f, T t) throws Exception;

}
