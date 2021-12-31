package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.AtchmnflDTO;
import com.example.dditlms.domain.dto.PresentnDTO;
import com.example.dditlms.domain.dto.TaskDTO;
import com.example.dditlms.mapper.OnlineLecMapper;
import com.example.dditlms.mapper.Taskmapper;
import com.example.dditlms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final Taskmapper mapper;
    private final OnlineLecMapper onlineLecMapper;

    //교수 과제 list
    @Override
    public void getProfessorTaskList(Map<String, Object> paramMap) {
        List<TaskDTO> taskList = mapper.getProfessorTaskList(paramMap);
        logger.info("taskList : " + taskList);

        paramMap.put("taskList", taskList);
    }

    //과제 등록
    @Override
    public void insertTask(Map<String, Object> paramMap) {
        String taskNm = paramMap.get("taskNm").toString();
        String taskCn = paramMap.get("taskCn").toString();
        String estblCoursCd = paramMap.get("estblCoursCd").toString();
        MultipartFile[] multipartFiles = (MultipartFile[]) paramMap.get("multipartFiles");
        Date taskPresentnTmlmt = null;
        Date taskDe = null;
        int atchmnflSn = 0;

        AtchmnflDTO atchmnflDTO = null;
        String filePath = "C:\\Users\\LMS\\Desktop\\Ddit\\finalProject\\DDITLMS-lecture\\DDITLMS-lecture\\src\\main\\resources\\static\\assignment";
        String randomName = UUID.randomUUID().toString().replaceAll("-","");
        String extension = null;
        String saveName = null;
        String orginalName = null;

        try {
            taskPresentnTmlmt =
                    new SimpleDateFormat("yyyy-mm-dd").parse(paramMap.get("taskPresentnTmlmt").toString());
            taskDe =
                    new SimpleDateFormat("yyyy-mm-dd").parse(paramMap.get("taskDe").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        File dir = new File(filePath);
        //파일 경로에 폴더가 없을 경우 만들기
        if(dir.exists() == false) {
            dir.mkdirs();
        }

        if(multipartFiles.length > 0) {
            int index = 1;
            for (MultipartFile file : multipartFiles) {
                extension = FilenameUtils.getExtension(file.getOriginalFilename());
                saveName = randomName + "." + extension;
                orginalName = file.getOriginalFilename();

                File target = new File(filePath, orginalName);
                try {
                    file.transferTo(target);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                atchmnflDTO = AtchmnflDTO.builder()
                        .atchmnflSn(0)
                        .fileSn(1)
                        .fileStreCours(filePath)
                        .streFileNm(saveName)
                        .orignlFileNm(orginalName)
                        .fileExtsn(extension)
                        .fileSize(file.getSize())
                        .jobSe("과제")
                        .dwldCo(0)
                        .build();

                index++;
                onlineLecMapper.insertAtchmnfl(atchmnflDTO);
                atchmnflSn = atchmnflDTO.getAtchmnflSn();
            }
        }
        logger.info("insertTaskServiceImpl - atchmnflSn : {}", atchmnflSn);

        TaskDTO taskDTO = TaskDTO.builder()
                .taskSn(0)
                .taskNm(taskNm)
                .taskCn(taskCn)
                .taskPresentnTmlmt(taskPresentnTmlmt)
                .taskDe(taskDe)
                .atchmnflId(atchmnflSn)
                .estblCoursCd(estblCoursCd)
                .build();
        logger.info("insertTaskServiceImpl - taskDTO : {}", taskDTO.toString());

        int result = mapper.insertTask(taskDTO);
        logger.info("insertTaskServiceImpl - result : {}", result);
        paramMap.put("result", result);
    }

    //등록한 과제 삭제 메소드
    @Override
    public void deleteTask(Map<String, Object> paramMap) {
        int taskSn = Integer.parseInt(paramMap.get("taskSn").toString());
        int atchmnflId = Integer.parseInt(paramMap.get("atchmnflId").toString());
        int deleteTaskResult = 0;
        int deleteAtchmnflResult = 0;

        deleteTaskResult = mapper.deleteTask(taskSn);

        if (deleteTaskResult > 0) {
            deleteAtchmnflResult = mapper.deleteAtchmnfl(atchmnflId);
        }

        paramMap.put("deleteTaskResult", deleteTaskResult);
        paramMap.put("deleteAtchmnflResult", deleteAtchmnflResult);
    }


    //학생 과제 list
    @Override
    public void getStudentTaskList(Map<String, Object> paramMap) {
        List<TaskDTO> taskList = mapper.getStudentTaskList(paramMap);
        List<AtchmnflDTO> taskAtchmnFileInfo = mapper.getAtchmnflList();
        List<PresentnDTO> presentnList = mapper.checkStudentPresentn(paramMap);
        logger.info("TaskServiceImpl - getStudentTaskList - presentnDTO : {}", taskList);

        paramMap.put("taskList", taskList);
        paramMap.put("taskAtchmnFileInfo", taskAtchmnFileInfo);
        paramMap.put("presentnList", presentnList);
    }

    //첨부파일 정보 확인
    @Override
    public AtchmnflDTO getAtchmnflInfo(int atchmnflId) {
        AtchmnflDTO atchmnflDTO = mapper.getAtchmnflInfo(atchmnflId);
        return atchmnflDTO;
    }

    //과제 제출
    @Override
    public void insertPresentn(Map<String,Object> paramMap) {
        int taskSn = Integer.parseInt(paramMap.get("taskSn").toString());
        int mberNo = Integer.parseInt(paramMap.get("mberNo").toString());
        String presentnSj = paramMap.get("presentnSj").toString();
        String presentnCn = paramMap.get("presentnCn").toString();
        MultipartFile[] multipartFiles = (MultipartFile[]) paramMap.get("multipartFiles");
        Date presentnDate = null;
        int atchmnflSn = 0;
        int taskScore = 0;

        AtchmnflDTO atchmnflDTO = null;
        String filePath = "C:\\Users\\LMS\\Desktop\\Ddit\\finalProject\\DDITLMS-lecture\\DDITLMS-lecture\\src\\main\\resources\\static\\assignment";
        String randomName = UUID.randomUUID().toString().replaceAll("-","");
        String extension = null;
        String saveName = null;
        String orginalName = null;

        try {
            presentnDate =
                    new SimpleDateFormat("yyyy-mm-dd").parse(paramMap.get("presentnDate").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        File dir = new File(filePath);
        //파일 경로에 폴더가 없을 경우 만들기
        if(dir.exists() == false) {
            dir.mkdirs();
        }

        if(multipartFiles.length > 0) {
            for (MultipartFile file : multipartFiles) {
                extension = FilenameUtils.getExtension(file.getOriginalFilename());
                saveName = randomName + "." + extension;
                orginalName = file.getOriginalFilename();

                File target = new File(filePath, orginalName);
                try {
                    file.transferTo(target);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                atchmnflDTO = AtchmnflDTO.builder()
                        .atchmnflSn(0)
                        .fileSn(1)
                        .fileStreCours(filePath)
                        .streFileNm(saveName)
                        .orignlFileNm(orginalName)
                        .fileExtsn(extension)
                        .fileSize(file.getSize())
                        .jobSe("과제 제출")
                        .dwldCo(0)
                        .build();

                onlineLecMapper.insertAtchmnfl(atchmnflDTO);
                atchmnflSn = atchmnflDTO.getAtchmnflSn();
            }
        }
        logger.info("TaskServiceImpl - insertPresentn - atchmnflSn : {}", atchmnflSn);

        PresentnDTO presentnDTO = PresentnDTO.builder()
                .mberNo(mberNo)
                .taskSn(taskSn)
                .presentnSj(presentnSj)
                .presentnCn(presentnCn)
                .presentnDate(presentnDate)
                .taskScore(taskScore)
                .atchmnflId(atchmnflSn)
                .build();
        logger.info("TaskServiceImpl - insertPresentn - taskDTO : {}", presentnDTO.toString());

        int result = mapper.insertPresentn(presentnDTO);
        logger.info("TaskServiceImpl - insertPresentn - result : {}", result);
        paramMap.put("result", result);
    }
}
