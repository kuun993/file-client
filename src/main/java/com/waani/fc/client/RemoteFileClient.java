package com.waani.fc.client;

import com.waani.fc.client.model.FileModel;
import java.io.InputStream;

public interface RemoteFileClient<T extends FileModel> {

    void createDir(T t) throws Exception;

    void uploadByPath(T t, String path) throws Exception;

    void uploadByInputStream(T t, InputStream inputStream) throws Exception;

    void download2File(T t, String path) throws Exception;

    InputStream download2Stream(T t) throws Exception;

    void removeFile(T t) throws Exception;

    void moveFile(T f, T t) throws Exception;

}
