package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import com.example.dditlms.domain.dto.SearchDTO;
import com.example.dditlms.service.ExamService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
    public void insertExam(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        logger.info("insertExam");
        logger.info("paramMap : " + paramMap);
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        /** 1.파라미터 조회*/
        /** 2.파라미터 검증(주요파라미터) */
        /** 3.서비스 처리 */
        /** 3-1.서비스 호출 파라미터 구성 */

        /** 3-2. 서비스 호출 */
        int result = examService.insertExam(paramMap);

        /** 4. 클라이언트 자료구성 */
        if (result == 0) jsonObject.put("state", "false");
        else jsonObject.put("state", "true");

        try {
            response.getWriter().print(jsonObject.toString());
        } catch (IOException e) {
            logger.error("{}", e);
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

        int result = examService.updateExam(paramMap);

        if (result == 0) jsonObject.put("state", "false");
        else jsonObject.put("state", "true");

        try {
            response.getWriter().print(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/exam/insertExamInfo")
    public void insertExamInfo(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        logger.info("insertExamInfo");
        logger.info("paramMap : " + paramMap);
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        /** 1.파라미터 조회*/
        /** TODO 테스트용 코드 */
        String estblCoursCd = "test001";
        paramMap.put("estblCoursCd", estblCoursCd);

        /** 2.파라미터 검증(주요파라미터) */
        /** 3.서비스 처리 */
        /** 3-1.서비스 호출 파라미터 구성 */

        int result = examService.insertExamInfo(paramMap);

        if (result == 0) jsonObject.put("state", "false");
        else jsonObject.put("state", "true");

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            logger.error("{}", e);
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
    public ModelAndView goExamPaper(ModelAndView mv,
                                    @ModelAttribute SearchDTO search,
                                    @RequestParam(required = false, defaultValue = "1") int pageNum,
                                    @RequestParam("examInfoCd") String examInfoCd) {
        logger.info("examPaper");
        /** 1.파라미터 조회*/
        Map<String, Object> examContentMap = new HashMap<>();
        examContentMap.put("pagNo", pageNum);
        examContentMap.put("examInfoCd", examInfoCd);
        examContentMap.put("search", search.getSearch());
        examContentMap.put("keyword", search.getKeyword());

        PageInfo<ExamDTO> paging = new PageInfo<>(examService.searchAndGetExamList(examContentMap), 1);
        logger.info("paging : " + paging);

        mv.addObject("examContentMap", examContentMap);
        mv.addObject("examInfoCd", examInfoCd);
        mv.addObject("examList", paging.getList());
        mv.addObject("users", paging);
        mv.addObject("search", search);
        mv.addObject("checkExamNumber", examContentMap.get("checkExamNumber"));
        mv.setViewName("pages/onlineLecture_professor/professor_lecture_examPaper");

        return mv;
    }



    /**------------------------------------------------------------------------------------------------------------------------------
     * 학생 part
     * ------------------------------------------------------------------------------------------------------------------------------*/
    //학생 시험
    @GetMapping("/onlineLecture/exam")
    public ModelAndView goOnlineLectureExam(ModelAndView mv) {
        logger.info("onlineExam");
        
        /**TODO 테스트 코드*/
        int mberNo = 201401449;

        /**변수 선언*/
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mberNo", mberNo);

        /**서비스 호출*/
        examService.getStudentExamInfo(paramMap);
        logger.info("studentExamInfo : " + paramMap.get("studentExamInfo"));

        /**값 전달*/
        mv.addObject("studentExamInfo", paramMap.get("studentExamInfo"));
        mv.addObject("progress", paramMap.get("progress"));
        mv.addObject("state", paramMap.get("state"));
        mv.addObject("mberNo", mberNo);
        mv.setViewName("pages/onlineLecture_student/student_lecture_exam");

        return mv;
    }

    //학생 시험 응시
    @GetMapping("/onlineLecture/examTest")
    public ModelAndView doOnlineExam(ModelAndView mv,
                                     @RequestParam("examInfoCd") String examInfoCd,
                                     @RequestParam("mberNo") int mberNo) {
        logger.info("doOnlineExam");
        logger.info("examInfoCd : {}, mberNo : {}",examInfoCd, mberNo);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mberNo", mberNo);
        paramMap.put("examInfoCd", examInfoCd);

        examService.getExamTestForStudent(paramMap);

        mv.addObject("examContentMap", paramMap.get("examContentMap"));
        mv.addObject("examInfoTimelimit", paramMap.get("examInfoTimelimit"));
        mv.addObject("mberNo", mberNo);
        mv.addObject("examList", paramMap.get("examList"));
        mv.setViewName("pages/onlineLecture_student/student_lecture_examProgress");

        return mv;
    }
}

