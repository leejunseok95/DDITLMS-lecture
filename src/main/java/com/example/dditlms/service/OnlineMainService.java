
package com.example.dditlms.service;

import java.util.List;
import java.util.Map;

public interface OnlineMainService {
    //학생이 수강 중인 과목 list
    void getStudentEstblCoursList(Map<String, Object> paramMap);
    //학생이 수강 중인 과목 일정
    void getStudentEstblcoursSchedule(Map<String, Object> paramMap);
    //교수가 가르치는 과목 list
    void getProfessorEstblCoursList(Map<String, Object> paramMap);
    //교수가 수강 중인 과목 일정
    void getProfessorEstblcoursSchedule(Map<String, Object> paramMap);
}
