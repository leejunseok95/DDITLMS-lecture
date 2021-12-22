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
    /**
     * 문제를 추가할때 선택할 수 있는 문항의 제한을 두기 위해 사용하는 쿼리
     * @param examInfoCd
     * @return 선택할 수 있는 문제 번호
     */
    List checkExamNumber(String examInfoCd);

    /**
     * 학생이 보는 시험 정보를 가져오기 위한 mapper
     * @param mberNo 접속한 학생의 학번
     * @return 학생 시험 정보
     */
    List<ExamInfoDTO> getStudentExamInfo(int mberNo);
}
