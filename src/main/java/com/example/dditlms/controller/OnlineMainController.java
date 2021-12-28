package com.example.dditlms.controller;

import com.example.dditlms.service.OnlineMainService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OnlineMainController {
    private static final Logger logger = LoggerFactory.getLogger(OnlineMainController.class);
    private final OnlineMainService service;

    @GetMapping("online/studentMain")
    public ModelAndView goStudentMainPage(ModelAndView mv) {
        /**TODO 임시 변수*/
        int mberNo = 201401449;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mberNo", mberNo);

        service.getStudentEstblCoursList(paramMap);
        service.getStudentEstblcoursSchedule(paramMap);

        logger.info("test : " + paramMap.get("studentEstbSchedule"));

        mv.setViewName("pages/onlineLecture_student/student_lecture_mainPage");
        mv.addObject("studentEstbList", paramMap.get("studentEstbList"));
        mv.addObject("studentEstbSchedule", paramMap.get("studentEstbSchedule"));
        return mv;
    }
}
