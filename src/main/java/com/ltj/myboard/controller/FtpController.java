package com.ltj.myboard.controller;

import com.ltj.myboard.dto.ftp.UserImageURL;
import com.ltj.myboard.service.FtpService;
import com.ltj.myboard.util.FileUtilExt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/ftp")
@RequiredArgsConstructor
@Slf4j
public class FtpController {
    @Value("${ftp.userfile.url}")
    private String ftpUserFileAccessUrl;

    @Value("${ftp.userfilepath}")
    private String userFilePath;

    private final FtpService ftpService;

    @GetMapping(
        value = "/userimage",
        produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<byte[]> getUserImage(@RequestParam(value = "filename") String fileName){
        try{
            log.info("try getUserImage.filename : " + fileName);
            byte[] data = ftpService.getFile(userFilePath, fileName);
            log.info("file size : " + data.length);
            return new ResponseEntity<byte[]>(data, HttpStatus.OK);
        }catch (Exception e){
            log.error("FtpController getUserImage 오류발생 : " + e.getMessage());
            return new ResponseEntity<byte[]>(new byte[]{}, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/userimage")
    public ResponseEntity<UserImageURL> uploadUserImage(@RequestParam MultipartFile upload){
        // 1. 파일 UUID Generate
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        // 2. 파일 byte Get
        byte[] bytes = null;
        try{

             bytes = upload.getBytes();
            log.info("FTPController 업로드 이미지 byte 크기: " + bytes.length);
        } catch (IOException e){
            log.error("UserFile Upload Failed: " + upload.getName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 3. FTP 업로드
        String fullFileName = upload.getOriginalFilename();
        String fileExtension = FileUtilExt.getFileExtension(fullFileName);
        String uploadFileName = uuidAsString + "." + fileExtension;
        log.info("FTPController 업로드 이미지 fullFileName: " + fullFileName);
        log.info("FTPController 업로드 이미지 fileExtension: " + fileExtension);
        log.info("FTPController 업로드 이미지 uploadFileName: " + uploadFileName);
        try{
            ftpService.uploadFile(userFilePath, uploadFileName, bytes);
        } catch (Exception e){
            log.error("FtpController uploadUserImage 오류발생 : " + e.getMessage());
            return new ResponseEntity<UserImageURL>(new UserImageURL(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 4. 업로드 경로 반환
        UserImageURL userImageURL = new UserImageURL();
        String imageRequestURL = ftpUserFileAccessUrl + "/ftp/userimage?filename=" + uploadFileName;
        userImageURL.setUrl(imageRequestURL);
        userImageURL.setUploadedFileID(uuidAsString);
        userImageURL.setUploadedFileName(uploadFileName);
        return new ResponseEntity<UserImageURL>(userImageURL, HttpStatus.OK);
    }

    @DeleteMapping("/userimage/{fileName}")
    public ResponseEntity deleteUserImage(@PathVariable("fileName") String fileName){
        // FTP 파일 제거
        try{
            ftpService.deleteFile(userFilePath, fileName);
        } catch (Exception e){
            log.error("FtpController deleteUserImage 오류발생 : " + e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
