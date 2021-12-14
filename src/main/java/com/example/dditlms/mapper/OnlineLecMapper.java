package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OnlineLecMapper {
    List<OnlineLecForPrintDTO> getOnlineLectureList(AtnlcLctreDTO atnlcLctreDTO);
    //위는 return 값이 listOnlineDTO지만 여기서는 Map을 이용하여 출력을 할 것
    List<OnlineLecForPrintDTO> getOnlineLectureListVer2(AtnlcLctreDTO atnlcLctreDTO);
    List<OnlineLecDTO> getProgessorOnlineLecutreList(EstblCoursDTO estblCoursDTO);
    AtchmnflDTO getOnlineLectureVideoName(String onlineLecCd);
    int saveOnlineLecVideoInfo(VidoInfoDTO vidoInfoDTO);
    VidoInfoDTO getVidoInfo(Map<String, Object> putVideoInfo);
    int checkVideoInfo(VidoInfoDTO vidoInfoDTO);
    int updateVideoInfo(VidoInfoDTO vidoInfoDTO);
    int insertAtchmnfl(AtchmnflDTO atchmnflDTO);
    int insertOnlineLecture(OnlineLecDTO onlineLecDTO);
    int updateOnlineLecLearningStatus(OnlineLecForPrintDTO onlineLecForPrintDTO);
    List<AtendDTO> getAtendInfo(Map<String, Object> putVideoInfo);
    void  insertAtendInfo(Map<String, Object> putVideoInfo);
    void updateAtendForOnlineLecture(AtendDTO atendDTO);
    OnlineLecForPrintDTO getOnlineLecInfoForCheckAtendInfo(String onlineLecCd);
}