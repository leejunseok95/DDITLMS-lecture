package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.BoardPager;
import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import com.example.dditlms.mapper.ExamMapper;
import com.example.dditlms.mapper.PagingMapper;
import com.example.dditlms.service.ExamService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);
    private final ExamMapper examMapper;
    private final PagingMapper pagingMapper;

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

        PageHelper.startPage(pageNo, 5);
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

    ////이거 지울거
    @Override
    public void getExamList(Map<String, Object> paramMap) {
        String examInfoCd = paramMap.get("examInfoCd").toString();
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
}
