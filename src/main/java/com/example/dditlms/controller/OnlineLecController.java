package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.*;
import com.example.dditlms.service.OnlineLecService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class OnlineLecController {
    private static final Logger logger = LoggerFactory.getLogger(OnlineLecController.class);

    private final OnlineLecService service;

    //학생 강의 목록
    @GetMapping("/student/onlineLecture")
    public ModelAndView goOnlineLecture(ModelAndView mv,
                                        HttpSession session,
                                        OnlineLecDTO onlineLecDTO,
                                        String estblCoursCd) {
        //수강신청 table atnlc_lctre 테이블에 있는 학번과 개설교과목코드를 통해 조회
        logger.info("OnlineLecController - goOnlineLecture - session mberNo : {}", session.getAttribute("stuMberNo"));
        logger.info("OnlineLecController - goOnlineLecture - estblCoursCd : {}",estblCoursCd);

        if(estblCoursCd != null) {
            session.setAttribute("stuEstblCoursCd", estblCoursCd);
        }

        /** 1.파라미터 조회(함수 파라미터, 세션 속성, 시스템변수) ****************************************************************/

        /** TODO 테스트용 코드 */
        int tempMberNo = Integer.parseInt(session.getAttribute("stuMberNo").toString());
        int insertVidoInfo = 0; // 수강신청 table atnlc_lctre 테이블에 있는 학번과 개설교과목코드를 통해 조회
        estblCoursCd = session.getAttribute("stuEstblCoursCd").toString();

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
        service.checkAtendInfo(paramMap);

        /** 4. 클라이언트 자료구성 */
        mv.addObject("onlineLecPrintList", paramMap.get("onlineLecPrintList"));
        mv.addObject("mberNo", tempMberNo);
        mv.setViewName("pages/onlineLecture_student/student_lecture_list");

        return mv;
    }

    @GetMapping("/student/onlineLecture/videoPlayer")
    public ModelAndView goOnlineLecVideoPlayer(ModelAndView mv,
                                               @RequestParam("onlineLecCd") String onlineLecCd,
                                               @RequestParam("mberNo") int mberNo) {
        logger.info("onlineLecCd : " + onlineLecCd);
        logger.info("mberNo : " + mberNo);

        Map<String, Object> putVideoInfo = new HashMap<>();
        putVideoInfo.put("mberNo", mberNo);
        putVideoInfo.put("onlineLecCd", onlineLecCd);
        AtchmnflDTO atchmnflDTO = service.getOnlineLectureVideoName(onlineLecCd);
        VidoInfoDTO vidoInfoDTO = service.getVidoInfo(putVideoInfo);

        logger.info("atchmnflDTO : " + atchmnflDTO);

        mv.setViewName("pages/onlineLecture_student/student_lecture_video");
        mv.addObject("atchmnflDTO", atchmnflDTO);
        mv.addObject("mberNo", mberNo);
        mv.addObject("onlineLecCd", onlineLecCd);
        mv.addObject("vidoInfoDTO", vidoInfoDTO);

        return mv;
    }

    //학생 온라인 강의 영상 정보 관리
    @PostMapping("/student/onlineLecture/uploadVideoInfo")
    public void saveOnlineLecVideoInfo(HttpServletResponse response,
                                       @RequestParam Map<String, String> paramMap) {
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
    @GetMapping("/student/onlineLecture/board")
    public String goOnlineLectureBoard() {
        logger.info("onlineBoard");
        return "pages/onlineLecture_student/student_lecture_board";
    }

    //-----------------------------------------------------------------------------
    /***********************************************교수 페이지****************************************/
    //-----------------------------------------------------------------------------

    //교수 강의 페이지
    @GetMapping("/professor/onlineLecture")
    public ModelAndView goProfessorOnlineLecture(ModelAndView mv,
                                                 HttpSession session,
                                                 String estblCoursCd) {
        logger.info("OnlineLecontroller - goProfessorOnlineLecture - mberNo : {}", session.getAttribute("proMberNo"));
        logger.info("OnlineLecontroller - goProfessorOnlineLecture - proEstblCoursCd session : {}", session.getAttribute("proEstblCoursCd"));

        if(estblCoursCd != null) {
            session.setAttribute("proEstblCoursCd", estblCoursCd);
        }

        //수강신청 table atnlc_lctre 테이블에 있는 학번과 개설교과목코드를 통해 조회
        int tempMberNo = Integer.parseInt(session.getAttribute("proMberNo").toString());
        estblCoursCd = session.getAttribute("proEstblCoursCd").toString();

        List<OnlineLecDTO> list = service.getProgessorOnlineLecutreList(new EstblCoursDTO(estblCoursCd, tempMberNo));
        list = list.stream().sorted(Comparator.comparing(OnlineLecDTO::getOnlineLecWeek)).collect(Collectors.toList());

        mv.setViewName("pages/onlineLecture_professor/professor_lecture_list");
        mv.addObject("onlineLecList", list);
        return mv;
    }

    //교수 게시판
    @GetMapping("/professor/onlineLecture/board")
    public String goProfessorOnlineLectureBoard() {
        logger.info("professorBoard");
        return "pages/onlineLecture_professor/professor_lecture_board";
    }

    //강의 업로드
    @PostMapping(value = "/professor/onlineLecture/uploadLecture", consumes = ("multipart/form-data"))
    public void uploadFile(HttpServletResponse response,
                           HttpSession session,
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

        /**TODO 임시 변수*/
        String estblCoursCd = session.getAttribute("proEstblCoursCd").toString();
        AtchmnflDTO atchmnflDTO = null;
        OnlineLecDTO onlineLecDTO = null;

        //update
//        String videoFilePath = "C:\\Users\\LMS\\Desktop\\Ddit\\finalProject\\DDITLMS-lecture\\DDITLMS-lecture\\src\\main\\resources\\static\\video";
        String videoFilePath = "C:\\DDITLMS-lecture\\src\\main\\resources\\static\\video";
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

//            onlineLecDTO
//                    = new OnlineLecDTO(null, Integer.parseInt(paramMap.get("onlineLecWeek").toString()),
//                    paramMap.get("onlineLecTitle").toString(), startDay, endDay, "-", atchmnflDTO.getAtchmnflSn(),
//                    estblCoursCd, "0", endTimeBuilder.toString());

            onlineLecDTO = OnlineLecDTO.builder()
                    .onlineLecCd(null)
                    .onlineLecWeek(Integer.parseInt(paramMap.get("onlineLecWeek").toString()))
                    .onlineLecTitle(paramMap.get("onlineLecTitle").toString())
                    .onlineLecStart(startDay)
                    .onlineLecEnd(endDay)
                    .learningStatus("-")
                    .atchmnflSn(atchmnflDTO.getAtchmnflSn())
                    .estblCoursCd(estblCoursCd)
                    .vidoEndtime(endTimeBuilder.toString())
                    .vidoStarttime("0")
                    .build();

            service.insertOnlineLecture(onlineLecDTO);
        }

        try {
            response.getWriter().print(jsonObject.toString());
            logger.info("jsonObject : " + jsonObject.toString());
        } catch (IOException e) {
            logger.error("{}",e);
        }
    }
}
