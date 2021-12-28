package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import com.example.dditlms.domain.dto.ExamResultDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExamMapper {
    //시험 (중간/기말) 정보를 가져오는 쿼리
    List<ExamInfoDTO> getExamInfoList(Map<String, Object> paramMap);
    //시험 문제 정보를 가지고 오는 쿼리
    ExamDTO getExamList(String examSn);
    ExamInfoDTO getExamInfo(String examInfoCd);

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

    /**
     * 학생이 보는 시험 문제를 가져오기 위한 mapper
     * @param paramMap 학생 번호와 문제지 키값
     * @return 시험 문제
     */
    List<ExamDTO> getExamTestForStudent(Map<String, Object> paramMap);

    /**
     * 학생이 처음 시험에 접속하게 되면 문제입력을 위한 값을 생성
     * @param examResultDTO 학생 답지
     * @return 성공 여부
     */
    int insertExamResult(ExamResultDTO examResultDTO);

    /**
     * 시험에 접속하여 문제를 입력/수정 하기 위한 mapper
     * @param examResultDTO 학생의 답안
     * @return 성공 여부 결과값
     */
    int updateExamResult(ExamResultDTO examResultDTO);

    /**
     * 문제입력을 위한 값의 유무를 판단하기 위한 mapper
     * null값이면 insert가 실행 아니면 그대로
     * @param examResultDTO 
     * @return 문제입력을 위한 값
     */
    ExamResultDTO checkExamResult(ExamResultDTO examResultDTO);

    /**
     * 시험 시간 정보를 가져오는 쿼리
     * @param examInfoCd 학생 시험 prikey
     * @return 시험 시간
     */
    int getExamTimeLimit(String examInfoCd);
}
