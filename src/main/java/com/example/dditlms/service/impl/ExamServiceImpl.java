package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import com.example.dditlms.domain.dto.ExamResultDTO;
import com.example.dditlms.domain.dto.ScoreDTO;
import com.example.dditlms.mapper.ExamMapper;
import com.example.dditlms.mapper.PagingMapper;
import com.example.dditlms.mapper.ScoreMapper;
import com.example.dditlms.service.ExamService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);
    private final ExamMapper examMapper;
    private final PagingMapper pagingMapper;
    private final ScoreMapper scoreMapper;

    @Override
    public List<ExamInfoDTO> getExamInfoList(Map<String, Object> paramMap) {
        /**파라미터 생성*/
        /**날짜를 계산, 비교해서 현재 시험이 진행 중인지 아닌지 설정*/
        String today = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Map<String, Object> progress = new HashMap<>(); //시험 진행 상태를 가져가기 위한 map
        int countExam = 0;

        /**로직 처리 구간*/
        List<ExamInfoDTO> examInfoList = examMapper.getExamInfoList(paramMap); // 시험(중간/기말) 정보 출력

        if (!examInfoList.isEmpty()) {
            for (ExamInfoDTO examInfoDTO : examInfoList) {
                Date examEndTime = null;
                Date examStartTime = examInfoDTO.getExamInfoDate();
                cal.add(Calendar.MINUTE, examInfoDTO.getExamInfoTimelimit());
                today = sdf.format(cal.getTime());

                countExam = countExamForExamInfo(examInfoDTO);
                paramMap.put("examCount", countExam);

                if(countExam >= 20) {
                    paramMap.put("examAvailability", "possible");
                } else {
                    paramMap.put("examAvailability", "impossible");
                }

                try {
                    examEndTime = sdf.parse(today);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date.compareTo(examStartTime) > 0) {
                    progress.put(examInfoDTO.getExamInfoCd(), "종료");
                } else if (date.compareTo(examStartTime) < 0 && examEndTime.compareTo(examStartTime) > 0) {
                    progress.put(examInfoDTO.getExamInfoCd(), "진행중");
                } else {
                    progress.put(examInfoDTO.getExamInfoCd(), "진행전");
                }
            }
            paramMap.put("progress", progress);
            paramMap.put("state", true);
        } else {
            paramMap.put("state", false);
        }

        return examMapper.getExamInfoList(paramMap);
    }

    @Override
    public Page<ExamDTO> searchAndGetExamList(Map<String, Object> paramsMap) {
        int pageNo = Integer.parseInt(paramsMap.get("pagNo").toString());

        PageHelper.startPage(pageNo, 20);
        Page<ExamDTO> list = pagingMapper.searchAndGetExamList(paramsMap);
        List checkExamNumberList = null;
        try {
            checkExamNumberList = checkExamNumber(paramsMap.get("examInfoCd").toString());
        }catch (NullPointerException e) {
            checkExamNumberList.add(0);
            logger.error("{}",e);
        }
        for (ExamDTO examDTO : list) {
            String examContent = null;
            String examType = examDTO.getExamType();
            try {
                examContent = examDTO.getExamContent();
            } catch (NullPointerException e) {

            }
            if (examType.equals("objective")) {
                String[] examContentList = examContent.split("/");
                paramsMap.put(examDTO.getExamSn() + "1", examContentList[0]);
                paramsMap.put(examDTO.getExamSn() + "2", examContentList[1]);
                paramsMap.put(examDTO.getExamSn() + "3", examContentList[2]);
                paramsMap.put(examDTO.getExamSn() + "4", examContentList[3]);
            }
        }

        paramsMap.put("checkExamNumber", checkExamNumberList);

        return list;
    }

    @Override
    public int insertExam(Map<String, Object> paramMap) {
        ExamDTO examDTO = ExamDTO.builder()
                .examSn(null)
                .examNumber(Integer.parseInt(paramMap.get("examNumber").toString()))
                .examTitle(paramMap.get("examTitle").toString())
                .examAnswer(paramMap.get("examAnswer").toString())
                .examType(paramMap.get("examType").toString())
                .examContent(paramMap.get("examContent").toString())
                .examInfoCd(paramMap.get("examInfoCd").toString())
                .build();

        return examMapper.insertExam(examDTO);
    }

    @Override
    public int insertExamInfo(Map<String, Object> paramMap) {
        String estblCoursCd = paramMap.get("estblCoursCd").toString();
        String paramDate = paramMap.get("examInfoDate").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date examInfoDate = null;

        //datetime-local 타입 변환
        try {
            examInfoDate = format.parse(paramDate);
        } catch (ParseException e) {
            logger.error("{}",e);
        }

        ExamInfoDTO examInfoDTO = ExamInfoDTO.builder()
                .examInfoCd(null)
                .examInfoTitle(paramMap.get("examInfoTitle").toString())
                .examInfoCategory(paramMap.get("examInfoCategory").toString())
                .examInfoDate(examInfoDate)
                .examInfoContent(paramMap.get("examInfoContent").toString())
                .estblCoursCd(estblCoursCd)
                .examInfoTimelimit(Integer.parseInt(paramMap.get("examInfoTimelimit").toString()))
                .build();


        return examMapper.insertExamInfo(examInfoDTO);
    }

    @Override
    public int updateExam(Map<String, Object> paramMap) {
        ExamDTO examDTO = ExamDTO.builder()
                .examSn(paramMap.get("examSn").toString())
                .examNumber(Integer.parseInt(paramMap.get("examNumber").toString()))
                .examTitle(paramMap.get("examTitle").toString())
                .examAnswer(paramMap.get("examAnswer").toString())
                .examType(paramMap.get("examType").toString())
                .examContent(paramMap.get("examContent").toString())
                .examInfoCd(paramMap.get("examInfoCd").toString())
                .build();

        return examMapper.updateExam(examDTO);
    }

    @Override
    public int updateExamInfo(ExamInfoDTO examInfoDTO) {
        return examMapper.updateExamInfo(examInfoDTO);
    }

    @Override
    public int deleteExamInfo(ExamInfoDTO examInfoDTO) {
        return examMapper.deleteExamInfo(examInfoDTO);
    }

    @Override
    public int deleteExam(ExamDTO examDTO) {
        return examMapper.deleteExam(examDTO);
    }

    @Override
    public int countExamForExamInfo(ExamInfoDTO examInfoDTO) {
        return examMapper.countExamForExamInfo(examInfoDTO);
    }

    @Override
    public List checkExamNumber(String examInfoCd) {
        return examMapper.checkExamNumber(examInfoCd);
    }

    @Override
    public void getStudentExamInfo(Map<String, Object> paramMap) {
        /**날짜를 계산, 비교해서 현재 시험이 진행 중인지 아닌지 설정*/
        String today = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Map<String, Object> progress = new HashMap<>();
        int mberNo = Integer.parseInt(paramMap.get("mberNo").toString());

        /**로직 처리 구간*/
        List<ExamInfoDTO> studentExamList = examMapper.getStudentExamInfo(paramMap);

        if (!studentExamList.isEmpty() || studentExamList != null) {
            for (ExamInfoDTO examInfoDTO : studentExamList) {
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
                    progress.put(examInfoDTO.getExamInfoCd(), "종료");
                } else if (date.compareTo(examStartTime) < 0 && examEndTime.compareTo(examStartTime) > 0) {
                    progress.put(examInfoDTO.getExamInfoCd(), "진행중");
                } else {
                    progress.put(examInfoDTO.getExamInfoCd(), "진행전");
                }
            }
            paramMap.put("progress", progress);
            paramMap.put("state", true);
        } else {
            paramMap.put("state", false);
        }

        paramMap.put("studentExamInfo", studentExamList);
    }

    @Override
    public void getExamTestForStudent(Map<String, Object> paramMap) {
        String examInfoCd = paramMap.get("examInfoCd").toString();
        int mberNo = Integer.parseInt(paramMap.get("mberNo").toString());
        Map<String, Object> examContentMap = new HashMap<>();

        List<ExamDTO> examList = examMapper.getExamTestForStudent(paramMap);
        int examInfoTimelimit = examMapper.getExamTimeLimit(examInfoCd);
        logger.info("시간 : "+examInfoTimelimit);

        for (ExamDTO examDTO : examList) {
            String examContent = null;
            String examType = examDTO.getExamType();

            try {
                examContent = examDTO.getExamContent();
            } catch (NullPointerException e) {
                logger.error("{}", e);
            }
            if (examType.equals("objective")) {
                String[] examContentList = examContent.split("/");
                examContentMap.put(examDTO.getExamSn() + "1", examContentList[0]);
                examContentMap.put(examDTO.getExamSn() + "2", examContentList[1]);
                examContentMap.put(examDTO.getExamSn() + "3", examContentList[2]);
                examContentMap.put(examDTO.getExamSn() + "4", examContentList[3]);
            }
        }
        paramMap.put("examContentMap", examContentMap);
        paramMap.put("examList", examList);
        paramMap.put("examInfoTimelimit", examInfoTimelimit);
    }

    @Override
    public void insertExamResult(Map<String, Object> paramMap) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> paramFromPage = (Map<String, Object>) paramMap.get("paramFromPage");
        List<ExamResultDTO> examResultList =
                mapper.convertValue(paramFromPage.get("answerList"), new TypeReference<List<ExamResultDTO>>() {});
        ScoreDTO scoreDTO = new ScoreDTO();

        int mberNo = Integer.parseInt(paramMap.get("mberNo").toString());
        String estblCoursCd = paramMap.get("estblCoursCd").toString();
        String examInfoCategory = null;
        logger.info("estblCoursCd : ", estblCoursCd);
        int middleScore = 0;
        int finalScore = 0;

        logger.info("examResultList : " + examResultList);

        for (ExamResultDTO examResultDTO : examResultList) {

            ExamDTO examDTO = examMapper.getExamList(examResultDTO.getExamSn());
            ExamInfoDTO examInfoDTO = examMapper.getExamInfo(examDTO.getExamInfoCd());
            examInfoCategory = examInfoDTO.getExamInfoCategory();

            examResultDTO = ExamResultDTO.builder()
                    .examResultSn(0)
                    .examInput(examResultDTO.getExamInput())
                    .mberNo(mberNo)
                    .examNumber(examResultDTO.getExamNumber())
                    .examSn(examResultDTO.getExamSn())
                    .build();

            if (checkExamResult(examResultDTO) == null) {
                examMapper.insertExamResult(examResultDTO);
            }

            logger.info("examDTO.getExamSn() : {}, examResultDTO.getExamSn : {}", examDTO.getExamSn(), examResultDTO.getExamSn());
            logger.info("examAnswer : {}, examInput : {}", examDTO.getExamAnswer(), examResultDTO.getExamInput());
            if(examDTO.getExamSn().equals(examResultDTO.getExamSn())) {
                if (examDTO.getExamAnswer().equals(examResultDTO.getExamInput()) && examInfoDTO.getExamInfoCategory().equals("middle")) {
                    middleScore += 5;
                } else if(examDTO.getExamAnswer().equals(examResultDTO.getExamInput()) && examInfoDTO.getExamInfoCategory().equals("final")){
                    finalScore += 5;
                }
            }
        }

        logger.info("middleScore : {}", middleScore);
        logger.info("finalScore : {}", finalScore);
        if(examInfoCategory.equals("middle")) {
            scoreDTO = ScoreDTO.builder()
                    .middleExprScore(middleScore)
                    .estblCoursCd(estblCoursCd)
                    .mberNo(mberNo)
                    .build();

        } else if(examInfoCategory.equals("final")) {
            scoreDTO = ScoreDTO.builder()
                    .trmendExprScore(finalScore)
                    .estblCoursCd(estblCoursCd)
                    .mberNo(mberNo)
                    .build();
        }

        logger.info("scoreDTO : " + scoreDTO.toString());

        int result = scoreMapper.updateScore(scoreDTO);

        paramMap.put("result", result);

    }

    @Override
    public void updateExamResult(Map<String, Object> paramMap) {

    }

    @Override
    public ExamResultDTO checkExamResult(ExamResultDTO examResultDTO) {
        return examMapper.checkExamResult(examResultDTO);
    }
}
