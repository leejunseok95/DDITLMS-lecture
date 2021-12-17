package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import com.example.dditlms.mapper.ExamMapper;
import com.example.dditlms.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);
    private final ExamMapper examMapper;

    @Override
    public List<ExamInfoDTO> getExamInfoList(Map<String, Object> paramMap) {
        return examMapper.getExamInfoList(paramMap);
    }

    @Override
    public int insertExam(ExamDTO examDTO) {
        logger.info("insertExam ExamServiceImpl examDTO : " + examDTO.toString());
        return examMapper.insertExam(examDTO);
    }

    @Override
    public int insertExamInfo(ExamInfoDTO examInfoDTO) {
        logger.info("insertExamInfo ExamServiceImpl examInfoDTO : " + examInfoDTO);
        return examMapper.insertExamInfo(examInfoDTO);
    }
}
