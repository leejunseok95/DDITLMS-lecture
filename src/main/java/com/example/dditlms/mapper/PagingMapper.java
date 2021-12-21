package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.ExamDTO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface PagingMapper {
    Page<ExamDTO> searchAndGetExamList(Map<String, Object> paramsMap);
}
