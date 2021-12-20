package com.example.dditlms.service;

import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;

import java.util.List;
import java.util.Map;

public interface ExamService {
    //시험 (중간/기말) 정보를 가져오는 쿼리
    List<ExamInfoDTO> getExamInfoList(Map<String, Object> paramMap);
    //시험 문제 정보를 가지고 오는 쿼리
    List<ExamDTO> getExamList(String examInfoCd);
    //문제 등록
    int insertExam(ExamDTO examDTO);
    //시험 등록
    int insertExamInfo(ExamInfoDTO examInfoDTO);
    //시험 수정
    int updateExamInfo(ExamInfoDTO examInfoDTO);
    //시험 삭제
    int deleteExamInfo(ExamInfoDTO examInfoDTO);
}
