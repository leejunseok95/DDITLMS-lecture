package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.AtchmnflDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OnlineMainMapper {
    /**
     * 학생이 수강 중인 과목 list를 가져오는 mapper
     * @param mberNo 학생 학번
     * @return 학생 수강 list
     */
    List<Map<String, Object>> getStudentEstblCoursList(int mberNo);

    /**
     * 학생이 수강 중인 과목 일정
     * @param mberNo 학생 학번
     * @return 학생 일정 list
     */
    List<Map<String, Object>> getStudentEstblcoursSchedule(int mberNo);
}
