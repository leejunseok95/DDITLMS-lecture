package com.example.dditlms.service;

import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface ScoreService {
    //입력한 강의에 적합한 학생 성적 정보
    void getStudentScore(Map<String, Object> paramMap);

    //입력한 강의에 적합한 학생 정보 페이징 처리
    Page<Map<String, Object>> searchAndGetStudentScoreList(Map<String, Object> paramMap);
}
