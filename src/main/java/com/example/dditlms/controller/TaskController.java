package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.AtchmnflDTO;
import com.example.dditlms.domain.dto.PresentnDTO;
import com.example.dditlms.domain.dto.TaskDTO;
import com.example.dditlms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService service;

    //교수 과제
    @GetMapping("/professorOnlineLecture/assignment")
    public ModelAndView goProfessorOnlineLectureAssignment(ModelAndView mv,
                                                           HttpSession session) {
        logger.info("TaskController - professorAssignment - mberNo sesseion : {}", session.getAttribute("proMberNo"));
        logger.info("TaskController - professorAssignment - estblCoursCd sesseion : {}", session.getAttribute("proEstblCoursCd"));
        /**TODO 임시 변수*/
        int mberNo = Integer.parseInt(session.getAttribute("proMberNo").toString());
        String estblCoursCd = session.getAttribute("proEstblCoursCd").toString();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mberNo", mberNo);
        paramMap.put("estblCoursCd", estblCoursCd);

        service.getProfessorTaskList(paramMap);
        //오케이 컨트롤러 딴에서 task_sn끼리 비교를 해서 따로 map에 넣어두고 출력을 하면 되겠구나
        List<TaskDTO> taskList = (List)paramMap.get("taskList");
        List<Map<String, Object>>  studentScoreList = (List)paramMap.get("studentScoreList");
        Map<String, Object> testpresentMap = (Map<String, Object>)paramMap.get("eachStudentScore");

        mv.setViewName("pages/onlineLecture_professor/professor_lecture_assignment");
        mv.addObject("taskList", paramMap.get("taskList"));
//        mv.addObject("studentCoursTakenList", paramMap.get("studentCoursTakenList"));
        mv.addObject("studentScoreList", studentScoreList);
        mv.addObject("eachStudentScore", paramMap.get("eachStudentScore"));
        mv.addObject("getTaskStudentNoAndNm", paramMap.get("getTaskStudentNoAndNm"));
        return mv;
    }

    //교수 과제 등록
    @PostMapping("/assignment/register")
    public void registerAssignment(HttpServletResponse response,
                                   HttpSession session,
                                   @RequestParam Map<String, Object> paramMap,
                                   @RequestParam("assignmentFile")MultipartFile[] multipartFiles) {
        logger.info("TaskController - registerAssignment - assignment register paramMap : {}", paramMap);
        logger.info("TaskController - registerAssignment - assignment register estblCoursCd : {}", session.getAttribute("proEstblCoursCd"));
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        /**TODO 임시 변수*/
        String estblCoursCd = session.getAttribute("proEstblCoursCd").toString();
        paramMap.put("estblCoursCd", estblCoursCd);
        paramMap.put("multipartFiles", multipartFiles);

        service.insertTask(paramMap);

        int result = Integer.parseInt(paramMap.get("result").toString());

        if (result > 0) {
            jsonObject.put("state", true);
        } else {
            jsonObject.put("state", false);
        }

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            logger.error("{}",e);
        }
        logger.info("TaskController - jsonObject : {}", jsonObject);
    }


    //업로드한 과제를 수정
    @PostMapping("/assignment/update")
    public void updateAssignment(HttpServletResponse response,
                                 @RequestParam Map<String, Object> paramMap,
                                 @RequestParam("assignmentFile")MultipartFile[] multipartFiles) {
        logger.info("updateAssignment");
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();
    }

    //업로드한 과제를 삭제
    @PostMapping("/assignment/delete")
    public void deleteAssignment(HttpServletResponse response,
                                 @RequestParam Map<String, Object> paramMap,
                                 @RequestParam("assignmentFile")MultipartFile[] multipartFiles) {
        logger.info("updateAssignment");
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        service.deleteTask(paramMap);

        int deleteTaskResult = Integer.parseInt(paramMap.get("deleteTaskResult").toString());
        int deleteAtchmnflResult = Integer.parseInt(paramMap.get("deleteAtchmnflResult").toString());

        if (deleteTaskResult > 0 && deleteAtchmnflResult > 0) {
            jsonObject.put("state", true);
        } else {
            jsonObject.put("state", false);
        }

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            logger.error("{}",e);
        }
    }

    /**
     * 학생 점수 update 메소드
     * @param response jsonObject를 보내기 위해 사용하는 메소드
     * @param paramMap 학생의 각 과제별 점수
     */
    @PostMapping("/assignment/score/update")
    public void updateStudentTaskScore(HttpServletResponse response,
                                       HttpSession session,
                                       @RequestParam Map<String, Object> paramMap) {
        logger.info("TaskController - updateStudentTaskScore - paramMap : {}", paramMap);
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        String estblCoursCd = session.getAttribute("proEstblCoursCd").toString();
        paramMap.put("estblCoursCd", estblCoursCd);

        service.updateStudentTaskScore(paramMap);

        int result = Integer.parseInt(paramMap.get("result").toString());

        if(result > 0) {
            jsonObject.put("state", true);
        } else {
            jsonObject.put("state", false);
        }

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            logger.error("{}",e);
        }
    }


    /******************************************학생 part*******************************************************/

    /**
     * 학생 과제 list 출력
     * @param mv 조회된 과제 list
     * @param session 학생 및 개설교과 session 번호
     * @return mv
     */
    @GetMapping("/onlineLecture/assignment")
    public ModelAndView goStudentOnlineLectureAssignment(ModelAndView mv,
                                                           HttpSession session) {
        logger.info("TaskController - studentAssignment - mberNo sesseion : {}", session.getAttribute("stuMberNo"));
        logger.info("TaskController - studentAssignment - estblCoursCd sesseion : {}", session.getAttribute("stuEstblCoursCd"));
        /**TODO 임시 변수*/
        int mberNo = Integer.parseInt(session.getAttribute("stuMberNo").toString());
        String estblCoursCd = session.getAttribute("stuEstblCoursCd").toString();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mberNo", mberNo);
        paramMap.put("estblCoursCd", estblCoursCd);

        service.getStudentTaskList(paramMap);

        mv.setViewName("pages/onlineLecture_student/student_lecture_assignment");
        mv.addObject("taskList", paramMap.get("taskList"));
        mv.addObject("taskAtchmnFileInfo", paramMap.get("taskAtchmnFileInfo"));
        mv.addObject("presentnList", paramMap.get("presentnList"));
        return mv;
    }


    /**
     * 과제 다운로드
     * @param atchmnflId 다운 받을 첨부파일 primary key
     * @param response 파일 인코딩, content 정보, 용량
     * @throws IOException
     */
    @GetMapping("/onlineLecture/assignment/download")
    public void downloadAssignmentFile(@RequestParam int atchmnflId,
                                                 HttpServletResponse response) throws IOException {
        logger.info("TaskController - downloadAssignmentFile - atchmnflId : {}", atchmnflId);

        AtchmnflDTO atchmnflDTO = service.getAtchmnflInfo(atchmnflId);
        String fileName = atchmnflDTO.getOrignlFileNm();
        String filePath = atchmnflDTO.getFileStreCours();

        byte[] fileByte = FileUtils.readFileToByteArray(new File(filePath+"\\"+fileName));

        response.setContentType("application/octet-stream");
        response.setContentLength(fileByte.length);
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.getOutputStream().write(fileByte);

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    //학생 과제 알집 다운로드
    @PostMapping("/assignment/downloadAll")
    public void downloadAllStudentAssignment(@RequestParam Map<String, Object> paramMap,
                                            HttpServletResponse response) {
        logger.info("TaskController - downloadAllStudentAssignment - paramMap : {}", paramMap);
        JSONObject jsonObject = new JSONObject();

        String zipFile = "C:\\Users\\LMS\\Downloads\\assignment.zip";
        String downloadFileName = "result";
        List<String> sourceFiles = new ArrayList<>();
        AtchmnflDTO atchmnflDTO = null;

        service.getStudentCoursTakenList(paramMap);

        List<TaskDTO> atchmnflIdList = (List<TaskDTO>)paramMap.get("atchmnflIdList");

        logger.info("TaskController - downloadAllStudentAssignment - atchmnflIdList : {}", atchmnflIdList);

        for(TaskDTO taskDTO : atchmnflIdList) {
            int atchmnflId = taskDTO.getAtchmnflId();

            if(atchmnflId != 0) {
                atchmnflDTO = service.getAtchmnflInfo(atchmnflId);
                sourceFiles.add(atchmnflDTO.getFileStreCours() + "\\" + atchmnflDTO.getOrignlFileNm());
            }
        }

        logger.info("TaskController - downloadAllStudentAssignment - atchmnflIdList : {}", atchmnflIdList);

        try {
            FileOutputStream fout = new FileOutputStream(zipFile);
            ZipOutputStream zout = new ZipOutputStream(fout);

            for(int i = 0; i< sourceFiles.size(); i++) {
                ZipEntry zipEntry = new ZipEntry(new File(sourceFiles.get(i)).getName());
                zout.putNextEntry(zipEntry);

                FileInputStream fin = new FileInputStream(sourceFiles.get(i));
                byte[] buffer = new byte[1024];
                int length;

                while((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }

                zout.closeEntry();
                fin.close();
            }

            zout.close();

            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=" + downloadFileName + "zip");
            jsonObject.put("state", true);


            FileInputStream fis = new FileInputStream(zipFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ServletOutputStream so = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(so);

            byte[] data = new byte[2048];
            int input = 0;

            while((input=bis.read(data)) != -1) {
                bos.write(data, 0, input);
                bos.flush();
            }

            if(bos!=null) bos.close();
            if(bis!=null) bis.close();
            if(so!=null) so.close();
            if(fis!=null) fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 학생 과제 제출
     * @param response JSONObject 설정값
     * @param session session에 저장된 학생 학번
     * @param paramMap page에서 ajax를 통해 보내온 과제 정보
     * @param multipartFiles 첨부파일
     */
    @PostMapping("/onlineLecture/assignment/submit")
    public void submitAssignment(HttpServletResponse response,
                                   HttpSession session,
                                   @RequestParam Map<String, Object> paramMap,
                                   @RequestParam("assignmentFile")MultipartFile[] multipartFiles) {
        logger.info("TaskController - submitAssignment - assignment submitAssignment paramMap : {}", paramMap);
        logger.info("TaskController - submitAssignment - sesseion mberNo : {}", session.getAttribute("stuMberNo"));
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();

        logger.info("TaskController - submitAssignment - submitAssignment multipartFiles : " + multipartFiles);

        /**TODO 임시 변수*/
        int mberNo = Integer.parseInt(session.getAttribute("stuMberNo").toString());
        paramMap.put("mberNo", mberNo);
        paramMap.put("multipartFiles", multipartFiles);

        service.insertPresentn(paramMap);

        int result = Integer.parseInt(paramMap.get("result").toString());

        if (result > 0) {
            jsonObject.put("state", true);
        } else {
            jsonObject.put("state", false);
        }

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            logger.error("{}",e);
        }
        logger.info("TaskController - submitAssignment - jsonObject : {}", jsonObject);
    }
}
