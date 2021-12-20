package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import com.example.dditlms.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ExamContoller {
    private static final Logger logger = LoggerFactory.getLogger(ExamContoller.class);
    private final ExamService examService;

    //교수 시험 페이지
    @GetMapping("/professorOnlineLecture/exam")
    public ModelAndView goProfessorOnlineLectureExam(ModelAndView mv) {
        logger.info("professorExam");

        /** 1.파라미터 조회*/
        /** TODO 테스트용 코드 */
        String estblCoursCd = "test001";
        int mberNo = 1;

        /** 2.파라미터 검증(주요파라미터) */
        /** 3.서비스 처리 */
        /** 3-1.서비스 호출 파라미터 구성 */
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("estblCoursCd", estblCoursCd);
        paramMap.put("mberNo", mberNo);

        /**3-2.서비스 호출*/
        List<ExamInfoDTO> examInfoList = examService.getExamInfoList(paramMap); // 시험(중간/기말) 정보 출력

        /** 4. 클라이언트 자료구성 */
        mv.addObject("progress", paramMap.get("progress"));
        mv.addObject("state", paramMap.get("state"));
        mv.addObject("examAvailability", paramMap.get("examAvailability"));
        mv.addObject("examInfoList", examInfoList);
        mv.setViewName("pages/onlineLecture_professor/professor_lecture_exam");

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
    //문제 수정  controller
    @PostMapping("/exam/updateExam")
    public void updateExam(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        /**1.파라미터 조회 ***************************************************************/
        logger.info("updateExam");
        logger.info("paramMap for update : " + paramMap);
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        ExamDTO examDTO = ExamDTO.builder()
                .examSn(paramMap.get("examSn").toString())
                .examNumber(Integer.parseInt(paramMap.get("examNumber").toString()))
                .examTitle(paramMap.get("examTitle").toString())
                .examAnswer(paramMap.get("examAnswer").toString())
                .examType(paramMap.get("examType").toString())
                .examContent(paramMap.get("examContent").toString())
                .examInfoCd(paramMap.get("examInfoCd").toString())
                .build();

        int result = examService.updateExam(examDTO);

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

    @PostMapping("/exam/updateExamInfo")
    public void updateExamInfo(HttpServletResponse response, HttpServletRequest request,
                               @RequestParam Map<String, String> paramMap) {
        logger.info("updateExamInfo");
        logger.info("paramMap updateExamInfo : " + paramMap);
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
                .examInfoCd(paramMap.get("examInfoCd"))
                .examInfoTitle(paramMap.get("examInfoTitle"))
                .examInfoCategory(paramMap.get("examInfoCategory"))
                .examInfoDate(examInfoDate)
                .examInfoContent(paramMap.get("examInfoContent"))
                .estblCoursCd(estblCoursCd)
                .examInfoTimelimit(Integer.parseInt(paramMap.get("examInfoTimelimit")))
                .build();

        logger.info("examInfoDTO for Update : " + examInfoDTO);
        result = examService.updateExamInfo(examInfoDTO);

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

    @PostMapping("/exam/deleteExamInfo")
    public void deleteExamInfo(HttpServletResponse response, HttpServletRequest request,
                               @RequestParam Map<String, String> paramMap) {
        logger.info("deleteExamInfo");
        logger.info("paramMap deleteExamInfo : " + paramMap);
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        //임시 개설교과 pk값
        int result = 0;

        ExamInfoDTO examInfoDTO = ExamInfoDTO.builder().examInfoCd(paramMap.get("examInfoCd")).build();

        result = examService.deleteExamInfo(examInfoDTO);

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

    //시험문제 삭제 controller
    @PostMapping("/exam/deleteExam")
    public void deleteExam(HttpServletResponse response, @RequestParam Map<String, String> paramMap) {
        logger.info("deleteExam");
        logger.info("paramMap deleteExam : " + paramMap);
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        ExamDTO examDTO = ExamDTO.builder().examSn(paramMap.get("examSn")).build();

        int result = examService.deleteExam(examDTO);

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
            if (examType.equals("objective")) {
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

