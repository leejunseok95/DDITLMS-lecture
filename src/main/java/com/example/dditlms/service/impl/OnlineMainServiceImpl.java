package com.example.dditlms.service.impl;

import com.example.dditlms.mapper.OnlineMainMapper;
import com.example.dditlms.service.OnlineMainService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OnlineMainServiceImpl implements OnlineMainService {
    private static final Logger logger = LoggerFactory.getLogger(OnlineMainServiceImpl.class);
    private final OnlineMainMapper mapper;

    @Override
    public void getStudentEstblCoursList(Map<String, Object> paramMap) {
        int mberNo = Integer.parseInt(paramMap.get("mberNo").toString());

        List<Map<String, Object>> studentEstbList = mapper.getStudentEstblCoursList(mberNo);
        paramMap.put("studentEstbList", studentEstbList);
    }

    @Override
    public void getStudentEstblcoursSchedule(Map<String, Object> paramMap) {
        int mberNo = Integer.parseInt(paramMap.get("mberNo").toString());

        List<Map<String, Object>> studentEstbSchedule = mapper.getStudentEstblcoursSchedule(mberNo);
        studentEstbSchedule = checkDayOfWeek(studentEstbSchedule);

        paramMap.put("studentEstbSchedule", studentEstbSchedule);
    }

    @Override
    public void getProfessorEstblCoursList(Map<String, Object> paramMap) {
        int mberNo = Integer.parseInt(paramMap.get("mberNo").toString());

        List<Map<String, Object>> professorEstbList = mapper.getProfessorEstblCoursList(mberNo);
        paramMap.put("professorEstbList", professorEstbList);
    }

    @Override
    public void getProfessorEstblcoursSchedule(Map<String, Object> paramMap) {
        int mberNo = Integer.parseInt(paramMap.get("mberNo").toString());

        List<Map<String, Object>> professorEstbSchedule = mapper.getProfessorEstblcoursSchedule(mberNo);
        professorEstbSchedule = checkDayOfWeek(professorEstbSchedule);

        paramMap.put("professorEstbSchedule",professorEstbSchedule);
    }

    //교수/학생의 스케줄을 확인하는 메소드
    public List<Map<String, Object>> checkDayOfWeek(List<Map<String, Object>> schedule) {
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();

        String day = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN);

        for(Map<String, Object> map : schedule) {
            String[] dayFromDb = map.get("LCTRUM_RESVE_USE_DE").toString().split(",");

            for (int i = 0; i < dayFromDb.length; i++) {
                logger.info("dayFromDb[i] : {} {}", dayFromDb[i], i);
                logger.info("day  : {} {}", day, i);
                if (dayFromDb[i].equals(day)) {
                    logger.info("dayFromDb : " + dayFromDb[i]);
                    map.put("today", "o");
                    break;
                } else {
                    map.put("today", "x");
                }
            }
        }

        return schedule;
    }
}
