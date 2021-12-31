package com.example.dditlms.service.impl;

import com.example.dditlms.mapper.ScoreMapper;
import com.example.dditlms.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private static final Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);
    private final ScoreMapper mapper;

    //입력한 강의에 적합한 학생의 성적 정보 출력
    @Override
    public void getStudentScore(Map<String, Object> paramMap) {
        logger.info("ScoreServiceImpl - getStudentScore - paramMap : {}", paramMap);
        String estblCoursCd = paramMap.get("estblCoursCd").toString();

        List<Map<String, Object>> studentScoreList = mapper.getStudentScore(estblCoursCd);

        paramMap.put("studentScoreList", studentScoreList);
    }
}
