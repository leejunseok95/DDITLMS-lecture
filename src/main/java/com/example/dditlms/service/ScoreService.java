package com.example.dditlms.service;

import java.util.List;
import java.util.Map;

public interface ScoreService {
    //입력한 강의에 적합한 학생 성적 정보
    void getStudentScore(Map<String, Object> paramMap);
}
