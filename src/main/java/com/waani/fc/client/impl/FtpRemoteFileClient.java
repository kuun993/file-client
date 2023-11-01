package com.waani.fc.client.impl;


import com.waani.fc.common.Constants;
import com.waani.fc.client.RemoteFileClient;
import com.waani.fc.client.model.FtpFileModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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
    public void createDir(FtpFileModel model) throws FtpProtocolException, IOException {
        String dir = model.getDir();
        if(StringUtils.isEmpty(dir)){
            return ;
        }
        String[] dirs = dir.split(Constants.SLASH);
        if(dirs.length == 0){
            return ;
        }
        StringBuilder stringBuilder = new StringBuilder() ;
        for (String d : dirs) {
            if(StringUtils.isEmpty(d)){
                continue;
            }
            stringBuilder.append(d) ;
            ftpClient.makeDirectory(stringBuilder.toString()) ;
            stringBuilder.append(Constants.SLASH) ;
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
        if(path!=null){
            FtpFileModel dir = new FtpFileModel() ;
            dir.setDir(path.toString());
            createDir(dir);
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
}
