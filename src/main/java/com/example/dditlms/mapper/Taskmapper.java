package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface Taskmapper {
    /**
     * 교수가 등록한 과제 list
     * @param paramMap 교수 번호, 개설교과 번호
     * @return 과제 list
     */
    List<TaskDTO> getProfessorTaskList(Map<String, Object> paramMap);

    /**
     * 학생이 보는 과제 list
     * @param paramMap 학생 번호, 수강 교과 번호
     * @return 과제 list
     */
    List<TaskDTO> getStudentTaskList(Map<String, Object> paramMap);

    /**
     * 교수 과제 등록
     * @param taskDTO 등록하는 과제 정보
     * @return 등록 결과
     */
    int insertTask(TaskDTO taskDTO);

    /**
     * 등록된 과제 제거
     * @param taskSn 과제 키값
     * @return 성공 여부
     */
    int deleteTask(int taskSn);

    /**
     * 같이 첨부된 첨부파일 삭제
     * @param atchmnflId 첨부파일 키값
     * @return 성공 여부
     */
    int deleteAtchmnfl(int atchmnflId);

    /**
     * 저장된 파일을 가져오기 윈한 mapper
     * @return 첨부파일
     */
    List<AtchmnflDTO> getAtchmnflList();

    /**
     * 파일 다운로드
     * @param atchmnflId 파일 키값
     * @return 파일 정보
     */
    AtchmnflDTO getAtchmnflInfo(int atchmnflId);

    /**
     * 과제 제출
     * @param presentnDTO 과제 정보
     * @return 과제 제출 성공 여부
     */
    int insertPresentn(PresentnDTO presentnDTO);

    /**
     * 과제 제출을 확인하기 위한 mapper
     * @param paramMap 학생 정보, 학생 수업 정보
     * @return 과제 제출 확인을 할 수 있는 결과 값
     */
    List<PresentnDTO> checkStudentPresentn(Map<String, Object> paramMap);

    /**
     * 수업을 듣는 학생들 list
     * @param estblCours 개설 과목 primary code
     * @return 학생 정보
     */
    List<Map<String, Object>> getStudentCoursTakenList(String estblCours);

    /**
     * 과제를 제출한 학생의 첨부파일 번호를 가져오기 위한 쿼리
     * @param taskSn 과제 번호
     * @return 과제를 제출한 학생의 정보(첨부파일 정보)
     */
    List<TaskDTO> getStudentPresentnAtchmnflId(int taskSn);

    /**
     * 과제 제출 내역을 check
     * @param presentnDTO 학생 학번, 과제 번호
     * @return 값이 있으면 1, 없으면 0
     */
    PresentnDTO checkPresentn(PresentnDTO presentnDTO);

    /**
     * 과제 내용 update mapper
     * @param presentnDTO 과제 정보
     * @return 과제 update 결과
     */
    int updatePresentnBySubmit(PresentnDTO presentnDTO);

    /**
     * 학생 과제 점수 및 정보를 가져오는 mapper
     * @param paramMap
     * @return
     */
    List<PresentnDTO> getTaskScore(Map<String, Object> paramMap);

    /**
     * 특정 강의를 수강하는 핛생의 학번과 이름을 가져오는 mapper
     * @param estblCoursCd 강의 primary key
     * @return 학생 학번, 이름
     */
    List<MberDTO> getTaskStudentNoAndNm(String estblCoursCd);

    /**
     * 과제 점수 update mapper
     * @param presentnDTO 학생 과제 점수, 과제 primary key, 학번
     * @return 제출 결과 값
     */
    int updateStudentTaskScore(PresentnDTO presentnDTO);

    /**
     * 과제 총 점수 작성
     * @param scoreDTO 과제 점수, 학생 학번, 강의 primary key
     * @return 결과
     */
    int updateScoreTasksCore(ScoreDTO scoreDTO);
}
