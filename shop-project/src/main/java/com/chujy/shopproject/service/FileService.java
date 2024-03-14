package com.chujy.shopproject.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();          // 파일명 중복 문제 해결

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + saveFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);     // 파일 출력 스트림을 만든다
        fos.write(fileData);                    // fileData 를 파일 출력 스트림에 입력
        fos.close();

        return saveFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);   // 파일이 저장된 경로를 이용하여 파일 객체 생성

        // 해당 파일이 존재하면 파일을 삭제하는 로직
        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
