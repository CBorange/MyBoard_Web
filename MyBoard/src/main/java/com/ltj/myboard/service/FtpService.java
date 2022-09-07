package com.ltj.myboard.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@Slf4j
public class FtpService {
    @Value("ftp.host")
    private String ftpHost;
    @Value("ftp.user")
    private String ftpUser;
    @Value("ftp.password")
    private String ftpPassword;

    public void uploadFile(String directoryPath, String fileName, byte[] fileBytes)
    {
        FTPClient ftpClient = new FTPClient();
        try {
           ftpClient.setControlEncoding("UTF-8");
           ftpClient.connect(ftpHost, 21);
            int resultCode = ftpClient.getReplyCode();

            if(!FTPReply.isPositiveCompletion(resultCode)){
                log.error("FTP 연결 실패: " + resultCode);
                throw new IllegalStateException("FTP 연결 실패: " + resultCode);
            }

            ftpClient.setSoTimeout(10000);
            if(!ftpClient.login(ftpUser, ftpPassword)){
                log.error("FTP 로그인 실패");
                throw new IllegalStateException("FTP 로그인 실패");
            }

            // 업로드
            ftpClient.makeDirectory(directoryPath);

            InputStream targetStream = new ByteArrayInputStream(fileBytes);
            String fileFullPath = directoryPath + "/" + fileName;
            if(!ftpClient.storeFile(fileFullPath, targetStream)) {
                int replyCode = ftpClient.getReplyCode();
                log.error("FTP 파일 업로드 실패: " + replyCode);
                throw new IllegalStateException("FTP 파일 업로드 실패: " + replyCode);
            }
        }catch (Exception e){
            log.error("FTP 업로드 알 수 없는 오류발생: " + e.getMessage());
            throw new IllegalStateException("FTP 업로드 알 수 없는 오류발생: " + e.getMessage());
        }
    }
}
