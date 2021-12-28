package com.example.dditlms.service.impl;

import com.example.dditlms.mapper.OnlineMainMapper;
import com.example.dditlms.service.OnlineMainService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.util.resources.LocaleData;

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
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();

        String day = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN);
        List<Map<String, Object>> studentEstbSchedule = mapper.getStudentEstblcoursSchedule(mberNo);

        for(Map<String, Object> map : studentEstbSchedule) {
            String[] dayFromDb = map.get("LCTRUM_RESVE_USE_DE").toString().split(",");

            for (int i = 0; i < dayFromDb.length; i++) {
                if (dayFromDb[i].equals(day)) {
                    logger.info("dayFromDb : " + dayFromDb[i]);
                    map.put("today", "o");
                }
            }
        }

        paramMap.put("studentEstbSchedule", studentEstbSchedule);
    }
}
