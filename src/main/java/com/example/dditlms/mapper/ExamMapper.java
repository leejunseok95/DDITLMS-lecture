package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExamMapper {
    //시험 (중간/기말) 정보를 가져오는 쿼리
    List<ExamInfoDTO> getExamInfoList(Map<String, Object> paramMap);
    //시험 문제 정보를 가지고 오는 쿼리
    List<ExamDTO> getExamList(String examInfoCd);

    //시험문제 정보 insert 하는 쿼리
    int insertExam(ExamDTO examDTO);
    // 시험 등록(중간/기말)하는 쿼리
    int insertExamInfo(ExamInfoDTO examInfoDTO);
    //시험 수정
    int updateExamInfo(ExamInfoDTO examInfoDTO);
    //시험 문제 삭제
    int updateExam(ExamDTO examDTO);
    //시험 삭제
    int deleteExamInfo(ExamInfoDTO examInfoDTO);
    //시험문제 삭제
    int deleteExam(ExamDTO examDTO);
    //시험문제 갯수 구하는 쿼리
    int countExamForExamInfo(ExamInfoDTO examInfoDTO);
}
