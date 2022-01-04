package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.SearchDTO;
import com.example.dditlms.service.ScoreService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
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
    @GetMapping("/professor/onlineLecture/management")
    public ModelAndView goProfessorOnlineLectureStudManagement(ModelAndView mv,
                                                               @ModelAttribute SearchDTO search,
                                                               @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                               HttpSession session) {
        logger.info("ScoreController - goProfessorOnlineLectureStudManagement - estblCoursCd session : {}", session.getAttribute("proEstblCoursCd"));
        String estblCoursCd = session.getAttribute("proEstblCoursCd").toString();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("estblCoursCd", estblCoursCd);
        paramMap.put("pageNum", pageNum);
        paramMap.put("search", search.getSearch());
        paramMap.put("keyword", search.getKeyword());

        service.getStudentScore(paramMap);
        PageInfo<Map<String, Object>> paging = new PageInfo<>(service.searchAndGetStudentScoreList(paramMap), 10);

        mv.setViewName("pages/onlineLecture_professor/professor_lecture_management");
        mv.addObject("studentScoreList", paramMap.get("studentScoreList"));
        mv.addObject("users", paging);
        mv.addObject("search", search);
        mv.addObject("paramMap", paramMap);

        return mv;
    }
}
