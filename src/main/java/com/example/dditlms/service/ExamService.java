package com.example.dditlms.service;

import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import com.github.pagehelper.Page;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface ExamService {
    //시험 (중간/기말) 정보를 가져오는 쿼리
    List<ExamInfoDTO> getExamInfoList(Map<String, Object> paramMap);
    //시험 문제 정보를 가지고 오는 쿼리
    void getExamList(Map<String, Object> paramMap);
    //문제 등록
    int insertExam(Map<String, Object> paramMap);
    //시험 등록
    int insertExamInfo(Map<String, Object> paramMap);
    //시험 문제 삭제
    int updateExam(Map<String, Object> paramMap);
    //시험 수정
    int updateExamInfo(ExamInfoDTO examInfoDTO);
    //시험 삭제
    int deleteExamInfo(ExamInfoDTO examInfoDTO);
    //시험문제 삭제
    int deleteExam(ExamDTO examDTO);
    //시험문제 갯수 구하는 쿼리
    int countExamForExamInfo(ExamInfoDTO examInfoDTO);
    //검색기능이 탑재된 exam 조회 쿼리
    Page<ExamDTO> searchAndGetExamList(Map<String, Object> paramsMap);
    //문제를 추가할때 선택할 수 있는 문항의 제한을 두기 위해 사용하는 쿼리
    List checkExamNumber(String examInfoCd);
}
