package com.example.dditlms.service;

import com.example.dditlms.domain.dto.*;

import java.util.List;
import java.util.Map;

public interface OnlineLecService {
    //학생 온라인 강의 리스트
    List<OnlineLecForPrintDTO> getOnlineLectureList(AtnlcLctreDTO atnlcLctreDTO);
    List<OnlineLecForPrintDTO> getOnlineLectureListVer2(AtnlcLctreDTO atnlcLctreDTO);
    //교수의 온라인 강의 영상 리스트
    List<OnlineLecDTO> getProgessorOnlineLecutreList(EstblCoursDTO estblCoursDTO);
    //온라인 강의에 있는 첨푸파일 목록을 가지고 옴
    AtchmnflDTO getOnlineLectureVideoName(String onlineLecCd);
    //시청하던 동영상 정보 저장
    int saveOnlineLecVideoInfo(VidoInfoDTO vidoInfoDTO);
    //이전에 들었던 영상을 이어서 듣기 위해 videoInfo를 가져오는 쿼리
    VidoInfoDTO getVidoInfo(Map<String, Object> putVideoInfo);
    //내가 들었던 영상인지 확인
    int checkVideoInfo(VidoInfoDTO vidoInfoDTO);
    //출석란에 필요한 값이 있는지 확인
    List<AtendDTO> getAtendInfo(Map<String, Object> putVideoInfo);
    //처음 온라인 강의에 접속했을 때 필요한 강의에 대한 출석값이 없다면 insert
    void  insertAtendInfo(Map<String, Object> putVideoInfo);
    int updateVideoInfo(VidoInfoDTO vidoInfoDTO);
    int insertAtchmnfl(AtchmnflDTO atchmnflDTO);
    int insertOnlineLecture(OnlineLecDTO onlineLecDTO);
    //온라인 강의 기간에 따른 학습현황 변경
    int updateOnlineLecLearningStatus(OnlineLecForPrintDTO onlineLecForPrintDTO);
    //강의 영상을 다 들었을 때 출석 체크
    void updateAtendForOnlineLecture(AtendDTO atendDTO);
    //출결체크 관리를 위해 온라인강의 진행률과 시간 정보를 가지고 오기
    OnlineLecForPrintDTO getOnlineLecInfoForCheckAtendInfo(String onlineLecCd);

    /** 온라인 강의 페이지 이동 */
    void selectGoOnlineLecture(Map<String, Object> paramMap);

    /**출석 점수 업데이트를 위한 쿼리*/
    void checkAtendInfo(Map<String, Object> paramMap);
}
