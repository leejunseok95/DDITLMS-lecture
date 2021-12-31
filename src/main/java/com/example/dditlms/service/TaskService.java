package com.example.dditlms.service;

import com.example.dditlms.domain.dto.AtchmnflDTO;
import com.example.dditlms.domain.dto.PresentnDTO;
import com.example.dditlms.domain.dto.TaskDTO;

import java.util.List;
import java.util.Map;

public interface TaskService {
    //교수가 등록한 과제 list
    void getProfessorTaskList(Map<String, Object> paramMap);
    //교수 과제 등록
    void insertTask(Map<String,Object> paramMap);
    //교수 과제 삭제
    void deleteTask(Map<String, Object> paramMap);
    //학생에게 출력되는 과제 list
    void getStudentTaskList(Map<String, Object> paramMap);
    //파일 다운로드
    AtchmnflDTO getAtchmnflInfo(int atchmnflId);
    //과제 제출
    void insertPresentn(Map<String,Object> paramMap);
}
