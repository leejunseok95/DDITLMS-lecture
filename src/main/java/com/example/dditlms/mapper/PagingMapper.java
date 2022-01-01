package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.ExamDTO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PagingMapper {
    Page<ExamDTO> searchAndGetExamList(Map<String, Object> paramsMap);

    /**
     * 강의를 듣고 있는 학생들의 정보를 paging 처리하기 위해 사용
     * @param paramMap 학생 개설교과 primary값, 검색 keyword
     * @return 페이지
     */
    Page<Map<String, Object>> searchAndGetStudentScoreList(Map<String, Object> paramMap);
}
