package com.ltj.myboard.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Slf4j
@Service
public class FtpService {
    @Value("${ftp.host}")
    private String ftpHost;
    @Value("${ftp.user}")
    private String ftpUser;
    @Value("${ftp.password}")
    private String ftpPassword;

    private FTPClient makeClient(){
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
            return ftpClient;
        } catch (Exception e){
            log.error("FTP 클라이언트 생성중 알 수 없는 오류발생: " + e.getMessage());
            throw new IllegalStateException("FTP 클라이언트 생성중 알 수 없는 오류발생: " + e.getMessage());
        }
    }

    public byte[] getFile(String directoryPath, String fileName) throws IOException {
        FTPClient ftpClient = null;
        try {
            // FTP Client 생성
            ftpClient = makeClient();

            // 파일 읽어오기
            InputStream inputStream = ftpClient.retrieveFileStream(directoryPath + "/" + fileName);
            if(inputStream != null){
                BufferedInputStream bfStream = new BufferedInputStream(inputStream);
                byte[] data = bfStream.readAllBytes();
                inputStream.close();
                ftpClient.completePendingCommand();
                return data;
            }
            else{
                log.error("FTP 파일을 찾을 수 없습니다.: " + fileName);
                throw new IllegalStateException("FTP 파일을 찾을 수 없습니다.: " + fileName);
            }
        } catch (Exception e) {
            log.error("FTP 파일읽기 알 수 없는 오류발생: " + e.getMessage());
            throw new IllegalStateException("FTP 파일읽기 알 수 없는 오류발생: " + e.getMessage());
        } finally {
            // FTP Client 해제
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public void uploadFile(String directoryPath, String fileName, byte[] fileBytes) throws IOException {
        FTPClient ftpClient = null;
        try {
            // FTP Client 생성
            ftpClient = makeClient();

            // 업로드
            String encoded = Base64.getEncoder().encodeToString(fileBytes);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.makeDirectory(directoryPath);

            InputStream targetStream = new ByteArrayInputStream(fileBytes);
            String fileFullPath = directoryPath + "/" + fileName;
            if(!ftpClient.storeFile(fileFullPath, targetStream)) {
                int replyCode = ftpClient.getReplyCode();
                log.error("FTP 파일 업로드 실패: " + replyCode);
                throw new IllegalStateException("FTP 파일 업로드 실패: " + replyCode);
            }
            targetStream.close();
        } catch (Exception e){
            log.error("FTP 업로드 알 수 없는 오류발생: " + e.getMessage());
            throw new IllegalStateException("FTP 업로드 알 수 없는 오류발생: " + e.getMessage());
        } finally {
            // FTP Client 해제
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public void deleteFile(String directoryPath, String fileName) throws IOException {
        FTPClient ftpClient = null;
        try {
            // FTP Client 생성
            ftpClient = makeClient();

            // FTP 파일 삭제
            String fileFullPath = directoryPath + "/" + fileName;
            if(!ftpClient.deleteFile(fileFullPath)){
                int replyCode = ftpClient.getReplyCode();
                log.error("FTP 파일삭제 실패: " + replyCode);
                throw new IllegalStateException("FTP 파일삭제 실패: " + replyCode);
            }
        } catch (Exception e){
            log.error("FTP 파일삭제 알 수 없는 오류발생: " + e.getMessage());
            throw new IllegalStateException("FTP 파일삭제 알 수 없는 오류발생: " + e.getMessage());
        } finally {
            // FTP Client 해제
            ftpClient.logout();
            ftpClient.disconnect();;
        }
    }
}
