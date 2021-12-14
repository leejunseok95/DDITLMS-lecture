package com.example.dditlms.utill;

import com.example.dditlms.domain.dto.AtchmnflDTO;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class FileUploadUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);

    public static String uploadVideoFolder =
            "C:\\Users\\LMS\\Desktop\\Ddit\\finalProject\\DDITLMS-lecture\\DDITLMS-lecture\\src\\main\\resources\\static\\video";
    public static String uploadImageFolder =
            "C:\\Users\\LMS\\Desktop\\Ddit\\finalProject\\DDITLMS-lecture\\DDITLMS-lecture\\src\\main\\resources\\static\\images";

    /**
     * 서버에 생성할 파일명을 처리할 랜덤 문자열 반환
     * @return 랜덤 문자열
     */
    private final String getRandomString() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 서버에 첨부파일을 생성하고, 업로드 파일 목록 반환
     * @param files
     * @param onlineLecCd
     * @return
     */
    public List<AtchmnflDTO> uploadVideoFiles(MultipartFile[] files, String onlineLecCd) {
        logger.info("서버에 첨부파일 생성");

        //파일이 비어 있으면 비어있는 리스트 반환
        if(files[0].getSize() < 1) {
            return Collections.emptyList();
        }

        //업로드 경로에 해당하는 디렉토리가 존재하지 않으면 모든 디렉토리를 생성
        File dir = new File(uploadVideoFolder);
        if(dir.exists() == false) {
            dir.mkdirs();
        }

        List<AtchmnflDTO> atchmnflList = new ArrayList<>();

        //파일 개수만큰 forEach 실행
        for(MultipartFile file : files) {
            try {
                //파일 확장자
                final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                //서버에 저장할 파일명(랜덤 문자열 + 확장자)
                final String saveName = getRandomString() + "." + extension;

                //업로드 경로에 saveName과 동일한 이름을 가진 파일 생성
                File target = new File(uploadVideoFolder, saveName);
                file.transferTo(target);

                //정보 저장
                AtchmnflDTO atchmnflDTO = new AtchmnflDTO();
                atchmnflDTO.setFileStreCours(uploadVideoFolder);
                atchmnflDTO.setStreFileNm(saveName);
                atchmnflDTO.setOrignlFileNm(file.getOriginalFilename());
                atchmnflDTO.setFileExtsn(extension);
                atchmnflDTO.setFileSize(file.getSize());
                atchmnflDTO.setJobSe("강의");

                //파일 정보 추가
                atchmnflList.add(atchmnflDTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return atchmnflList;
    }
}
