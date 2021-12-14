package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.*;
import com.example.dditlms.mapper.AtchmnflMapper;
import com.example.dditlms.mapper.OnlineLecMapper;
import com.example.dditlms.service.OnlineLecService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OnlineLecServiceImpl implements OnlineLecService {
    private static final Logger logger = LoggerFactory.getLogger(OnlineLecServiceImpl.class);

    private final OnlineLecMapper onlineLecMapper;

    @Override
    public List<OnlineLecForPrintDTO> getOnlineLectureList(AtnlcLctreDTO atnlcLctreDTO) {
        List<OnlineLecForPrintDTO> list = onlineLecMapper.getOnlineLectureList(atnlcLctreDTO);
        return list;
    }

    @Override
    public List<OnlineLecForPrintDTO> getOnlineLectureListVer2(AtnlcLctreDTO atnlcLctreDTO) {
        return onlineLecMapper.getOnlineLectureListVer2(atnlcLctreDTO);
    }

    @Override
    public List<OnlineLecDTO> getProgessorOnlineLecutreList(EstblCoursDTO estblCoursDTO) {
        return onlineLecMapper.getProgessorOnlineLecutreList(estblCoursDTO);
    }

    @Override
    public AtchmnflDTO getOnlineLectureVideoName(String onlineLecCd) {
        return onlineLecMapper.getOnlineLectureVideoName(onlineLecCd);
    }

    @Override
    public int saveOnlineLecVideoInfo(VidoInfoDTO vidoInfoDTO) {
        return onlineLecMapper.saveOnlineLecVideoInfo(vidoInfoDTO);
    }

    @Override
    public VidoInfoDTO getVidoInfo(Map<String, Object> putVideoInfo) {
        return onlineLecMapper.getVidoInfo(putVideoInfo);
    }

    @Override
    public int checkVideoInfo(VidoInfoDTO vidoInfoDTO) {
        return onlineLecMapper.checkVideoInfo(vidoInfoDTO);
    }

    @Override
    public List<AtendDTO> getAtendInfo(Map<String, Object> putVideoInfo) {
        return onlineLecMapper.getAtendInfo(putVideoInfo);
    }

    @Override
    public void insertAtendInfo(Map<String, Object> putVideoInfo) {
        onlineLecMapper.insertAtendInfo(putVideoInfo);
    }

    @Override
    public int updateVideoInfo(VidoInfoDTO vidoInfoDTO) {
        return onlineLecMapper.updateVideoInfo(vidoInfoDTO);
    }

    @Override
    public int insertAtchmnfl(AtchmnflDTO atchmnflDTO) {
        return onlineLecMapper.insertAtchmnfl(atchmnflDTO);
    }

    @Override
    public int insertOnlineLecture(OnlineLecDTO onlineLecDTO) {
        return onlineLecMapper.insertOnlineLecture(onlineLecDTO);
    }

    @Override
    public int updateOnlineLecLearningStatus(OnlineLecForPrintDTO onlineLecForPrintDTO) {
        return onlineLecMapper.updateOnlineLecLearningStatus(onlineLecForPrintDTO);
    }

}
