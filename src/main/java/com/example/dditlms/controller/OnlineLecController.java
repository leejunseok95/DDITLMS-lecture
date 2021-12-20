package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.*;
import com.example.dditlms.service.OnlineLecService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.model.Model;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class OnlineLecController {
    private static final Logger logger = LoggerFactory.getLogger(OnlineLecController.class);

    private final OnlineLecService service;

    //학생 강의 목록
    @GetMapping("/onlineLecture")
    public ModelAndView goOnlineLecture(ModelAndView mv, OnlineLecDTO onlineLecDTO) {
        logger.info("lectureList");

        /** 1.파라미터 조회(함수 파라미터, 세션 속성, 시스템변수) ****************************************************************/

        /** TODO 테스트용 코드 */
        int tempMberNo = 201401449;
        int insertVidoInfo = 0; // 수강신청 table atnlc_lctre 테이블에 있는 학번과 개설교과목코드를 통해 조회
        String estblCoursCd = "test001";

        /** 2.파라미터 검증(주요파라미터) */
        /** 3.서비스 처리 */
        /** 3-1. 서비스 호출 파라미터 구성 */
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tempMberNo", tempMberNo);
        paramMap.put("insertVidoInfo", insertVidoInfo);
        paramMap.put("estblCoursCd", estblCoursCd);
        paramMap.put("onlineLecDTO", onlineLecDTO);

        /** 3-2. 서비스 호출 */
        service.selectGoOnlineLecture(paramMap); // save, select, get, update, delete, merge

        /** 4. 클라이언트 자료구성 */
        mv.addObject("onlineLecPrintList", paramMap.get("onlineLecPrintList"));
        mv.addObject("mberNo", tempMberNo);
        mv.setViewName("pages/onlineLecture_student/student_lecture_list");

        return mv;

//        logger.info("lectureList");
//        ModelAndView mv = new ModelAndView("pages/onlineLecture_student/student_lecture_list");
//        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        LocalDate now = LocalDate.now();
//        //수강신청 table atnlc_lctre 테이블에 있는 학번과 개설교과목코드를 통해 조회
//        int insertVidoInfo = 0;
//        int tempMberNo = 201401449;
//        String estblCoursCd = "test001";
//        //강의 영상 정보를 입력
//        VidoInfoDTO vidoInfoDTO = null;
//        Map<String, Object> putVideoInfo = new HashMap<>();
//        AtnlcLctreDTO atnlcLctreDTO = new AtnlcLctreDTO(tempMberNo, estblCoursCd, 0);
//        List<AtendDTO> atendList = null;
//
//        List<OnlineLecForPrintDTO> onlineLecForVideoInfo =
//                onlineLecService.getOnlineLectureList(atnlcLctreDTO);
//        logger.info("onlineLecForVideoInfo : " + onlineLecForVideoInfo.toString());
//
//        for(OnlineLecForPrintDTO onlineLecForPrintDTOTest : onlineLecForVideoInfo) {
//            putVideoInfo.put("mberNo", tempMberNo);
//            putVideoInfo.put("estblCoursCd", estblCoursCd);
//            putVideoInfo.put("onlineLecCd", onlineLecForPrintDTOTest.getOnlineLecCd());
//            vidoInfoDTO = onlineLecService.getVidoInfo(putVideoInfo);
//
//            atendList = onlineLecService.getAtendInfo(putVideoInfo);
//
//            if(atendList.isEmpty()) {
//                onlineLecService.insertAtendInfo(putVideoInfo);
//            }
//
//            if(vidoInfoDTO == null) {
//                vidoInfoDTO = VidoInfoDTO.builder()
//                        .vidoInfoSn(0)
//                        .mberNo(tempMberNo)
//                        .vidoPlaytime(0)
//                        .vidoProgress(0)
//                        .onlineLecCd(onlineLecForPrintDTOTest.getOnlineLecCd())
//                        .build();
//                insertVidoInfo = onlineLecService.saveOnlineLecVideoInfo(vidoInfoDTO);
//            }
//        }
//
//        List<OnlineLecForPrintDTO> onlineLecPrintList =
//                onlineLecService.getOnlineLectureListVer2(atnlcLctreDTO);
//        onlineLecPrintList =
//                onlineLecPrintList.stream().sorted(Comparator.comparing(OnlineLecForPrintDTO::getOnlineLecWeek)).collect(Collectors.toList());
//
//        logger.info("onlineLecListMap Version 2 : " + onlineLecPrintList.toString());
//
//        int updateLearningStaResult = 0;
//        AtendDTO atendDTO = null;
//        for(OnlineLecForPrintDTO onlineLecForPrintDTO : onlineLecPrintList) {
//            try {
//                Date startLectureDay = sdf.parse(onlineLecForPrintDTO.getOnlineLecStart().toString());
//                Date endLectureDay = sdf.parse(onlineLecForPrintDTO.getOnlineLecEnd().toString());
//                Date nowDate = dateFormat.parse(now.toString());
//                int videoProgress = onlineLecForPrintDTO.getVidoProgress();
//
//                if(startLectureDay.before(nowDate) && endLectureDay.after(nowDate)) {
//                    logger.info("학습진행");
//                    onlineLecForPrintDTO.setLearningStatus("학습진행");
//                } else if(startLectureDay.after(nowDate)) {
//                    logger.info("학습예정");
//                    onlineLecForPrintDTO.setLearningStatus("학습예정");
//                } else if(endLectureDay.before(nowDate)) {
//                    logger.info("학습종료");
//                    onlineLecForPrintDTO.setLearningStatus("학습종료");
//
//                    if (videoProgress < 50) {
//                        atendDTO =
//                                new AtendDTO("n", nowDate, tempMberNo, onlineLecForPrintDTO.getOnlineLecCd());
//                        onlineLecService.updateAtendForOnlineLecture(atendDTO);
//                    } else if(videoProgress >= 50 && videoProgress <= 95) {
//                        atendDTO =
//                                new AtendDTO("o", nowDate, tempMberNo, onlineLecForPrintDTO.getOnlineLecCd());
//                        onlineLecService.updateAtendForOnlineLecture(atendDTO);
//                    }
//                }
//                updateLearningStaResult = onlineLecService.updateOnlineLecLearningStatus(onlineLecForPrintDTO);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        mv.addObject("onlineLecPrintList", onlineLecPrintList);
//        mv.addObject("mberNo", tempMberNo);
//
//        return mv;

    }

    @GetMapping("/goOnlineLecVideoPlayer")
    public ModelAndView goOnlineLecVideoPlayer(@RequestParam("onlineLecCd") String onlineLecCd,
                                               @RequestParam("mberNo") int mberNo, Model model) {
        logger.info("onlineLecCd : " + onlineLecCd);
        logger.info("mberNo : " + mberNo);

        ModelAndView mv = new ModelAndView("pages/onlineLecture_student/student_lecture_video");
        Map<String, Object> putVideoInfo = new HashMap<>();
        putVideoInfo.put("mberNo", mberNo);
        putVideoInfo.put("onlineLecCd", onlineLecCd);
        AtchmnflDTO atchmnflDTO = service.getOnlineLectureVideoName(onlineLecCd);
        VidoInfoDTO vidoInfoDTO = service.getVidoInfo(putVideoInfo);

        logger.info("atchmnflDTO : " + atchmnflDTO);

        mv.addObject("atchmnflDTO", atchmnflDTO);
        mv.addObject("mberNo", mberNo);
        mv.addObject("onlineLecCd", onlineLecCd);
        mv.addObject("vidoInfoDTO", vidoInfoDTO);
        logger.info("vidoInfoDTO : " + vidoInfoDTO.toString());

        return mv;
    }

    //학생 온라인 강의 영상 정보 관리
    @PostMapping("/onlineLecture/uploadVideoInfo")
    public void saveOnlineLecVideoInfo(HttpServletResponse response, HttpServletRequest request,
                                       @RequestParam Map<String, String> paramMap) {
        logger.info("uploadVideoInfo param :" + paramMap);
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        int mberNo = Integer.parseInt(paramMap.get("mberNo"));
        String onlineLecCd = paramMap.get("onlineLecCd");
        OnlineLecForPrintDTO onlineLecForPrintDTO
                = service.getOnlineLecInfoForCheckAtendInfo(onlineLecCd);
        VidoInfoDTO vidoInfoDTO = null;

        Map<String, Object> putVideoInfo = new HashMap<>();
        putVideoInfo.put("mberNo", mberNo);
        putVideoInfo.put("onlineLecCd", onlineLecCd);
        putVideoInfo.put("estblCoursCd", "");
        logger.info("putvideoInfo : " + putVideoInfo);

        List<AtendDTO> atendDTOList = service.getAtendInfo(putVideoInfo);
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        logger.info("오늘날 date now : " + now);

        int getVidoInfoSn = service.getVidoInfo(putVideoInfo).getVidoInfoSn();
        int vidoInfoSn = Integer.parseInt(paramMap.get("vidoInfoSn"));
        int videoProgress = Integer.parseInt(paramMap.get("vidoProgress"));
        Date startDay = onlineLecForPrintDTO.getOnlineLecStart();
        Date endDay = onlineLecForPrintDTO.getOnlineLecEnd();

        if(videoProgress == 100) {
            logger.info("영상 100% 시청 완료");
            if(startDay.before(now) && endDay.after(now)) {
                service.updateAtendForOnlineLecture(
                        new AtendDTO("y", now, mberNo, onlineLecCd));
            }
        }

        if(getVidoInfoSn == vidoInfoSn) {
            vidoInfoDTO = VidoInfoDTO.builder()
                    .vidoInfoSn(vidoInfoSn)
                    .mberNo(Integer.parseInt(paramMap.get("mberNo")))
                    .vidoPlaytime(Integer.parseInt(paramMap.get("vidoPlaytime")))
                    .vidoProgress(Integer.parseInt(paramMap.get("vidoProgress")))
                    .onlineLecCd(paramMap.get("onlineLecCd"))
                    .build();

            service.updateVideoInfo(vidoInfoDTO);
        }
        logger.info("vidoInfoDTO : " + vidoInfoDTO);
    }

    //학생 게시판
    @GetMapping("/onlineLecture/board")
    public String goOnlineLectureBoard() {
        logger.info("onlineBoard");
        return "pages/onlineLecture_student/student_lecture_board";
    }

    //학생 과제
    @GetMapping("/onlineLecture/assignment")
    public String goOnlineLectureAssignment() {
        logger.info("onlineAssignment");
        return "pages/onlineLecture_student/student_lecture_assignment";
    }

    //학생 시험
    @GetMapping("/onlineLecture/exam")
    public String goOnlineLectureExam() {
        logger.info("onlineExam");
        return "pages/onlineLecture_student/student_lecture_exam";
    }

    //-----------------------------------------------------------------------------
    //교수 페이지
    //-----------------------------------------------------------------------------

    //교수 강의 페이지
    @GetMapping("/professorOnlineLecture")
    public ModelAndView goProfessorOnlineLecture(Model model) {
        logger.info("professorOnlineLecture");
        ModelAndView mv = new ModelAndView("pages/onlineLecture_professor/professor_lecture_list");
        JSONObject jsonObject = new JSONObject();
        //수강신청 table atnlc_lctre 테이블에 있는 학번과 개설교과목코드를 통해 조회
        int tempMberNo = 1;
        String estblCoursCd = "test001";

        List<OnlineLecDTO> list = service.getProgessorOnlineLecutreList(new EstblCoursDTO(estblCoursCd, tempMberNo));
        list = list.stream().sorted(Comparator.comparing(OnlineLecDTO::getOnlineLecWeek)).collect(Collectors.toList());
        mv.addObject("onlineLecList", list);

        return mv;
    }

    //교수 게시판
    @GetMapping("/professorOnlineLecture/board")
    public String goProfessorOnlineLectureBoard() {
        logger.info("professorBoard");
        return "pages/onlineLecture_professor/professor_lecture_board";
    }

    //교수 과제
    @GetMapping("/professorOnlineLecture/assignment")
    public String goProfessorOnlineLectureAssignment() {
        logger.info("professorAssignment");
        return "pages/onlineLecture_professor/professor_lecture_assignment";
    }

    //교수 학생 관리
    @GetMapping("/professorOnlineLecture/management")
    public String goProfessorOnlineLectureStudManagement() {
        logger.info("professorStudentManagement");
        return "pages/onlineLecture_professor/professor_lecture_management";
    }

    //강의 업로드
    @PostMapping(value = "/uploadLecture", consumes = ("multipart/form-data"))
    public void uploadFile(HttpServletResponse response, HttpServletRequest request,
                           @RequestParam Map<String, Object> paramMap,
                           @RequestParam("onlineLectureFile") MultipartFile[] multipartFile) throws ParseException {
        //파일 경로가 제대로 왔는지 확인
        logger.info("uploadFile");
        logger.info("paramMap : " + paramMap);

        for (MultipartFile file : multipartFile) {
            logger.info("filename : " + file.getOriginalFilename());
        }

        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        JSONObject jsonObject = new JSONObject();

        String estblCoursCd = "test001";
        AtchmnflDTO atchmnflDTO = null;
        OnlineLecDTO onlineLecDTO = null;

        String videoFilePath = "C:\\Users\\LMS\\Desktop\\Ddit\\finalProject\\DDITLMS-lecture\\DDITLMS-lecture\\src\\main\\resources\\static\\video";
        String randomName = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = null;
        String saveName = null;
        String originalName = null;
        int atchResult = 0;

        File dir = new File(videoFilePath);
        if (dir.exists() == false) {
            dir.mkdirs();
        }

        if (multipartFile.length > 0) {
            for (MultipartFile file : multipartFile) {
                try {
                    extension = FilenameUtils.getExtension(file.getOriginalFilename());
                    saveName = randomName + "." + extension;
                    originalName = file.getOriginalFilename();

                    //check ----
                    //파일 저장 이름을 변경해서 저장
                    //저장된 이름을 불러와서 그에 맞는 경로에 설정
                    File target = new File(videoFilePath, originalName);
                    file.transferTo(target);

                    atchmnflDTO = new AtchmnflDTO(0, 1, videoFilePath, saveName, file.getOriginalFilename(),
                            extension, file.getSize(), "강의", 0);

                    atchResult = service.insertAtchmnfl(atchmnflDTO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (atchResult == 0) {
            jsonObject.put("state", "false");
        } else {
            jsonObject.put("state", "true");

            double parameEndTimeDouble = 0;
            int min, hour, sec;
            try {
                parameEndTimeDouble = Double.parseDouble(paramMap.get("onlineLectureTime").toString());
            } catch (Exception e) {
            }

            int parameEndTime = (int) parameEndTimeDouble;
            logger.info("parameEndTime : " + parameEndTime);

            min = parameEndTime / 60;
            hour = min / 60;
            sec = parameEndTime % 60;
            min = min % 60;
            //String builder로 변경
            StringBuilder endTimeBuilder = new StringBuilder();
            endTimeBuilder.append(hour + ":" + min + ":" + sec);
//            String endTime = hour + ":" + min + ":" + sec;

            Date startDay = new SimpleDateFormat("yyyy-MM-dd").parse(paramMap.get("onlineLecStart").toString());
            Date endDay = new SimpleDateFormat("yyyy-MM-dd").parse(paramMap.get("onlineLecEnd").toString());

            logger.info("onlineLectureTime : " + paramMap.get("onlineLectureTime"));

            onlineLecDTO
                    = new OnlineLecDTO(null, Integer.parseInt(paramMap.get("onlineLecWeek").toString()),
                    paramMap.get("onlineLecTitle").toString(), startDay, endDay, "-", atchmnflDTO.getAtchmnflSn(),
                    estblCoursCd, "0", endTimeBuilder.toString());

            service.insertOnlineLecture(onlineLecDTO);
        }

        try {
            response.getWriter().print(jsonObject.toString());
            logger.info("jsonObject : " + jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
