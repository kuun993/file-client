package com.waani.fc.client.impl;


import com.waani.fc.common.Constants;
import com.waani.fc.client.RemoteFileClient;
import com.waani.fc.client.model.FtpFileModel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpDirEntry;
import sun.net.ftp.FtpProtocolException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;


/**
 * @author waani
 * @date 2023
 * @email kuun993@163.com
 * @description FtpRemoteFileClient
 */
@RequiredArgsConstructor
public class FtpRemoteFileClient implements RemoteFileClient<FtpFileModel> {

    
    private final FtpClient ftpClient ;
    

    @Override
    public void createDirectory(FtpFileModel model) throws FtpProtocolException, IOException {
        String remoteDirectory = model.getRemoteDirectory();
        if(StringUtils.isEmpty(remoteDirectory)){
            return ;
        }
        createDirectory(remoteDirectory);
    }


    /**
     * 创建目录
     * @param remoteDirectory
     */
    private void createDirectory(String remoteDirectory) throws IOException, FtpProtocolException {
        String[] dirs = remoteDirectory.split(Constants.SLASH);
        if(dirs.length == 0){
            return ;
        }
        String workingDirectory = ftpClient.getWorkingDirectory();
        for (String dir : dirs) {
            if(StringUtils.isEmpty(dir)){
                continue;
            }
            changeDirectory(dir);
        }
        ftpClient.changeDirectory(workingDirectory);
    }


    /**
     * changeDirectory
     * @param remoteDirectory
     * @throws IOException
     * @throws FtpProtocolException
     */
    private void changeDirectory(String remoteDirectory) throws IOException, FtpProtocolException {
        try {
            ftpClient.changeDirectory(remoteDirectory);
        } catch (Exception e) {
            ftpClient.makeDirectory(remoteDirectory) ;
            ftpClient.changeDirectory(remoteDirectory);
        }
    }



    @Override
    public void uploadByPath(FtpFileModel model, String path) throws IOException, FtpProtocolException {
        uploadByInputStream(model, Files.newInputStream(Paths.get(path))) ;
    }

    @Override
    public void uploadByInputStream(FtpFileModel model, InputStream inputStream) throws FtpProtocolException, IOException {
        mkdir(model) ;
        ftpClient.putFile(model.getRemoteName(), inputStream) ;
    }

    protected void mkdir(FtpFileModel model) throws IOException, FtpProtocolException{
        Path path = Paths.get(model.getRemoteName()).getParent();
        if(path != null){
            FtpFileModel dir = new FtpFileModel() ;
            dir.setRemoteDirectory(path.toString());
            createDirectory(dir);
        }
    }


    @Override
    public void download2File(FtpFileModel model, String path) throws IOException, FtpProtocolException {
        IOUtils.copy(download2Stream(model), Files.newOutputStream(Paths.get(path))) ;
    }

    @Override
    public InputStream download2Stream(FtpFileModel model) throws FtpProtocolException, IOException {
        return ftpClient.getFileStream(model.getRemoteName());
    }

    @Override
    public void removeFile(FtpFileModel model) throws FtpProtocolException, IOException {
        ftpClient.deleteFile(model.getRemoteName()) ;
    }


    @Override
    public void moveFile(FtpFileModel from, FtpFileModel to) throws FtpProtocolException, IOException {
        ftpClient.rename(from.getRemoteName(), to.getRemoteName()) ;
    }

    @Override
    public void removeDirectoryAndFiles(FtpFileModel ftpFileModel) throws FtpProtocolException, IOException {
        String remoteDirectory = ftpFileModel.getRemoteDirectory();
        removeDirectoryAndFiles(remoteDirectory);
    }



    private void removeDirectoryAndFiles(String remoteDirectory) throws FtpProtocolException, IOException {
        // 删除目录下的文件
        Iterator<FtpDirEntry> iterator = ftpClient.listFiles(remoteDirectory);
        while (iterator.hasNext()) {
            FtpDirEntry next = iterator.next();
            String name = remoteDirectory + Constants.SLASH + next.getName();
            if (FtpDirEntry.Type.FILE.equals(next.getType())) {
                ftpClient.deleteFile(name);
                continue;
            }
            removeDirectoryAndFiles(name);
        }
    }
}
