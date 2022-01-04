package com.example.dditlms.controller;

import com.example.dditlms.service.OnlineMainService;
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
public class OnlineMainController {
    private static final Logger logger = LoggerFactory.getLogger(OnlineMainController.class);
    private final OnlineMainService service;

    @GetMapping("/student/online/studentMain")
    public ModelAndView goStudentMainPage(ModelAndView mv, HttpSession session) {
        /**TODO 임시 변수*/
//        int mberNo = 201401450;
        int mberNo = 201401449;
        session.setAttribute("stuMberNo", mberNo);

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

//    @PostMapping("/online/main/onlineLecture")
//    public String goOnlineLecture(@RequestParam Map<String, Object> paramMap,
//                                  RedirectAttributes redirectAttributes) {
//        String estblCoursCd = paramMap.get("estblCoursCd").toString();
//        logger.info("test : " + paramMap.toString());
//
//        redirectAttributes.addFlashAttribute("estblCoursCd", estblCoursCd);
//
//        return "redirect:/onlineLecture";
//    }


    /**********************************************교수님  part*********************************/

    @GetMapping("/professor/online/professorMain")
    public ModelAndView goProfessorMainPage(ModelAndView mv, HttpSession session) {
        /**TODO 임시 변수*/
        int mberNo = 9999;
        session.setAttribute("proMberNo", mberNo);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mberNo", mberNo);

        service.getProfessorEstblCoursList(paramMap);
        service.getProfessorEstblcoursSchedule(paramMap);

        logger.info("test professor : " + paramMap.get("professorEstbSchedule"));

        mv.setViewName("pages/onlineLecture_professor/professor_lecture_mainPage");
        mv.addObject("professorEstbList", paramMap.get("professorEstbList"));
        mv.addObject("professorEstbSchedule", paramMap.get("professorEstbSchedule"));
        return mv;
    }
}
