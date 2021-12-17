package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import com.example.dditlms.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Model;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExamContoller {
    private static final Logger logger = LoggerFactory.getLogger(ExamContoller.class);
    private final ExamService examService;

    //교수 시험
    @GetMapping("/professorOnlineLecture/exam")
    public ModelAndView goProfessorOnlineLectureExam() {
        logger.info("professorExam");
        //임시적으로 사용하는 값
        String estblCoursCd = "test001";
        int mberNo = 1;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("estblCoursCd", estblCoursCd);
        paramMap.put("mberNo", mberNo);

        ModelAndView mv = new ModelAndView("pages/onlineLecture_professor/professor_lecture_exam");
        List<ExamInfoDTO> examInfoList = examService.getExamInfoList(paramMap);

        mv.addObject("examInfoList", examInfoList);

        return mv;
    }

    @PostMapping("/exam/insertExam")
    public void insertExam(HttpServletResponse response, HttpServletRequest request,
                           @RequestParam Map<String, Object> paramMap) {
        logger.info("insertExam");
        logger.info("paramMap : " + paramMap);
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        ExamDTO examDTO = ExamDTO.builder()
                .examSn(null)
                .examNumber(1)
                .examTitle(paramMap.get("examTitle").toString())
                .examAnswer(paramMap.get("examAnswer").toString())
                .examType(paramMap.get("examType").toString())
                .examContent(paramMap.get("examContent").toString())
                .examInfoCd("")
                .build();

        int result = examService.insertExam(examDTO);

        if(result == 0) {
            jsonObject.put("state", "false");
        } else {
            jsonObject.put("state", "true");
        }

        try {
            response.getWriter().print(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/exam/insertExamInfo")
    public void insertExamInfo(HttpServletResponse response, HttpServletRequest request,
                                @RequestParam Map<String, String> paramMap) {
        logger.info("insertExamInfo");
        logger.info("paramMap : " + paramMap);
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        String estblCoursCd = "test001";

        //datetime-local 타입 변환
        String paramDate = paramMap.get("examInfoDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date examInfoDate = null;
        try {
            examInfoDate = format.parse(paramDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        logger.info("examInfoDate : test : " + examInfoDate);

        ExamInfoDTO examInfoDTO = ExamInfoDTO.builder()
                .examInfoCd(null)
                .examInfoTitle(paramMap.get("examInfoTitle"))
                .examInfoCategory(paramMap.get("examInfoCategory"))
                .examInfoDate(examInfoDate)
                .examInfoContent(paramMap.get("examInfoContent"))
                .estblCoursCd(estblCoursCd)
                .examInfoTimelimit(Integer.parseInt(paramMap.get("examInfoTimelimit")))
                .build();

        logger.info("examInfoDTO : " + examInfoDTO);

        int result = examService.insertExamInfo(examInfoDTO);

        if(result == 0) {
            jsonObject.put("state", "false");
        } else {
            jsonObject.put("state", "true");
        }

        try {
            response.getWriter().print(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
