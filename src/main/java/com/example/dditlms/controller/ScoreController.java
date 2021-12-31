package com.example.dditlms.controller;

import com.example.dditlms.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ScoreController {
    private static final Logger logger = LoggerFactory.getLogger(ScoreController.class);
    private final ScoreService service;

    //교수 학생 관리
    @GetMapping("/professorOnlineLecture/management")
    public ModelAndView goProfessorOnlineLectureStudManagement(ModelAndView mv,
                                                               HttpSession session) {
        logger.info("ScoreController - goProfessorOnlineLectureStudManagement - estblCoursCd session : {}", session.getAttribute("proEstblCoursCd"));
        String estblCoursCd = session.getAttribute("proEstblCoursCd").toString();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("estblCoursCd", estblCoursCd);

        service.getStudentScore(paramMap);

        mv.setViewName("pages/onlineLecture_professor/professor_lecture_management");
        mv.addObject("studentScoreList", paramMap.get("studentScoreList"));

        return mv;
    }
}
