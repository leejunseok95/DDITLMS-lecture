package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.BoardPager;
import com.example.dditlms.domain.dto.ExamDTO;
import com.example.dditlms.domain.dto.ExamInfoDTO;
import com.example.dditlms.mapper.ExamMapper;
import com.example.dditlms.service.ExamService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);
    private final ExamMapper examMapper;

    @Override
    public List<ExamInfoDTO> getExamInfoList(Map<String, Object> paramMap) {
        List<ExamInfoDTO> examInfoList = examMapper.getExamInfoList(paramMap); // 시험(중간/기말) 정보 출력
        /**파라미터 생성*/
        /**날짜를 계산, 비교해서 현재 시험이 진행 중인지 아닌지 설정*/
        String today = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Map<String, Object> progress = new HashMap<>(); //시험 진행 상태를 가져가기 위한 map
        int countExam = 0;

        /**로직 처리 구간*/
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
    public List<ExamDTO> getExamList(String examInfoCd) {
        return examMapper.getExamList(examInfoCd);
    }

    @Override
    public int insertExam(ExamDTO examDTO) {
        logger.info("insertExam ExamServiceImpl examDTO : " + examDTO.toString());
        return examMapper.insertExam(examDTO);
    }

    @Override
    public int insertExamInfo(ExamInfoDTO examInfoDTO) {
        logger.info("insertExamInfo ExamServiceImpl examInfoDTO : " + examInfoDTO);
        return examMapper.insertExamInfo(examInfoDTO);
    }

    @Override
    public int updateExam(ExamDTO examDTO) {
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
}
