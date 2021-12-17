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
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ExamContoller {
    private static final Logger logger = LoggerFactory.getLogger(ExamContoller.class);
    private final ExamService examService;

    //교수 시험 페이지
    @GetMapping("/professorOnlineLecture/exam")
    public ModelAndView goProfessorOnlineLectureExam() {
        logger.info("professorExam");
        //임시적으로 사용하는 값
        //--------------------------------------------
        String estblCoursCd = "test001";
        int mberNo = 1;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("estblCoursCd", estblCoursCd);
        paramMap.put("mberNo", mberNo);
        //--------------------------------------------
        //날짜를 계산, 비교해서 현재 시험이 진행 중인지 아닌지 설정
        String today = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        //--------------------------------------------

        ModelAndView mv = new ModelAndView("pages/onlineLecture_professor/professor_lecture_exam");
        List<ExamInfoDTO> examInfoList = examService.getExamInfoList(paramMap);
        Map<String, Object> progress = new HashMap<>();

        if (!examInfoList.isEmpty()) {
            for (ExamInfoDTO examInfoDTO : examInfoList) {
                Date examEndTime = null;
                Date examStartTime = examInfoDTO.getExamInfoDate();
                cal.add(Calendar.MINUTE, examInfoDTO.getExamInfoTimelimit());
                today = sdf.format(cal.getTime());

                try {
                    examEndTime = sdf.parse(today);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date.compareTo(examStartTime) > 0) {
                    progress.put(examInfoDTO.getExamInfoCd(), "진행전");
                } else if (date.compareTo(examStartTime) < 0 && examEndTime.compareTo(examStartTime) > 0) {
                    progress.put(examInfoDTO.getExamInfoCd(), "진행중");
                } else {
                    progress.put(examInfoDTO.getExamInfoCd(), "종료");
                }
            }
            mv.addObject("progress", progress);
            mv.addObject("state", true);
        } else {
            mv.addObject("state", false);
        }

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
                .examNumber(Integer.parseInt(paramMap.get("examNumber").toString()))
                .examTitle(paramMap.get("examTitle").toString())
                .examAnswer(paramMap.get("examAnswer").toString())
                .examType(paramMap.get("examType").toString())
                .examContent(paramMap.get("examContent").toString())
                .examInfoCd(paramMap.get("examInfoCd").toString())
                .build();

        int result = examService.insertExam(examDTO);

        if (result == 0) {
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

        //임시 개설교과 pk값
        String estblCoursCd = "test001";
        int result = 0;

        //datetime-local 타입 변환
        String paramDate = paramMap.get("examInfoDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date examInfoDate = null;
        try {
            examInfoDate = format.parse(paramDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        result = examService.insertExamInfo(examInfoDTO);

        if (result == 0) {
            jsonObject.put("state", "false");
        } else {
            jsonObject.put("state", "true");
        }

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/exam/examPaper")
    public ModelAndView goExamPaper(@RequestParam("examInfoCd") String examInfoCd) {
        logger.info("examPaper");
        logger.info("examInfoCd : " + examInfoCd);

        ModelAndView mv = new ModelAndView("pages/onlineLecture_professor/professor_lecture_examPaper");
        List<ExamDTO> examList = examService.getExamList(examInfoCd);
        examList = examList.stream().sorted(Comparator.comparing(ExamDTO::getExamNumber)).collect(Collectors.toList());
        Map<String, Object> examContentMap = new HashMap<>();

        for (ExamDTO examDTO : examList) {
            String examContent = null;
            String examType = examDTO.getExamType();
            try {
                examContent = examDTO.getExamContent();
                logger.info("examContent : " + examContent);
            } catch (NullPointerException e) {

            }
            if (examType.equals("subjective")) {
                String[] examContentList = examContent.split("/");
                logger.info("examcontentList : " + examContentList);
                examContentMap.put(examDTO.getExamSn() + "1", examContentList[0]);
                examContentMap.put(examDTO.getExamSn() + "2", examContentList[1]);
                examContentMap.put(examDTO.getExamSn() + "3", examContentList[2]);
                examContentMap.put(examDTO.getExamSn() + "4", examContentList[3]);
                logger.info("examContentMap : " + examContentMap);
            }
        }

        mv.addObject("examContentMap", examContentMap);
        mv.addObject("examInfoCd", examInfoCd);
        mv.addObject("examList", examList);

        return mv;
    }
}
