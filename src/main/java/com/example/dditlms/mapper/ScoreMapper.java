package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.AtendDTO;
import com.example.dditlms.domain.dto.ScoreDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper {
    /**
     * 학생 성적을 등록하기 위해 미리 학생에 대한 성적값을 insert
     * @param scoreDTO 학생 정보
     * @return 학생 성적값이 입력 성공 여부
     */
    int insertStudentScore(ScoreDTO scoreDTO);

    /**
     * 학생 성적 컬럼 데이터가 생성되어 있는지 확인하는 유무
     * @param scoreDTO 학생 정보
     * @return 학생 성적 정보
     */
    ScoreDTO checkStudentScore(ScoreDTO scoreDTO);

    /**
     * 학생 성적 정보를 update를 사용하기 위한 mapper
     * @param scoreDTO 학생 성적 정보
     * @return 학생 수정 성공 여부
     */
    int updateScore(ScoreDTO scoreDTO);
    int updateScoreForOther(ScoreDTO scoreDTO);

    /**
     * 성적 점수를 내기 위해 select를 하는 쿼리
     * @param atendDTO 학생 수강 정보
     * @return 학생 출석 현황
     */
    List<AtendDTO> checkAtendInfo(AtendDTO atendDTO);
}
