package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.*;
import com.example.dditlms.mapper.AtchmnflMapper;
import com.example.dditlms.mapper.OnlineLecMapper;
import com.example.dditlms.mapper.ScoreMapper;
import com.example.dditlms.service.OnlineLecService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnlineLecServiceImpl implements OnlineLecService {
    private static final Logger logger = LoggerFactory.getLogger(OnlineLecServiceImpl.class);

    private final OnlineLecMapper onlineLecMapper;
    private final ScoreMapper scoreMapper;

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
        logger.info("putVideoInfo in onlineLecServiceImpl getAtendInfo: " + putVideoInfo);
        return onlineLecMapper.getAtendInfo(putVideoInfo);
    }

    @Override
    public void insertAtendInfo(Map<String, Object> putVideoInfo) {
        logger.info("putVideoInfo in onlineLecServiceImpl: " + putVideoInfo);
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

    @Override
    public void updateAtendForOnlineLecture(AtendDTO atendDTO) {
        onlineLecMapper.updateAtendForOnlineLecture(atendDTO);
    }

    @Override
    public OnlineLecForPrintDTO getOnlineLecInfoForCheckAtendInfo(String onlineLecCd) {
        return onlineLecMapper.getOnlineLecInfoForCheckAtendInfo(onlineLecCd);
    }

    @Override
    public void selectGoOnlineLecture(Map<String, Object> paramMap) {
        /** ???????????? ?????? ****************************************************************************************/
        int tempMberNo = Integer.parseInt(paramMap.get("tempMberNo").toString());
        int insertVidoInfo = (int) paramMap.get("insertVidoInfo");
        String estblCoursCd = (String) paramMap.get("estblCoursCd");
        OnlineLecDTO onlineLecDTO = (OnlineLecDTO) paramMap.get("onlineLecDTO");

        logger.debug("goOnlineLecture{} - {}", insertVidoInfo, estblCoursCd);

        /** ???????????? ?????? */
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        VidoInfoDTO vidoInfoDTO = null; //?????? ?????? ????????? ??????
        AtnlcLctreDTO atnlcLctreDTO = AtnlcLctreDTO.builder()
                .mberNo(tempMberNo)
                .estblCoursCd(estblCoursCd)
                .pnt(0)
                .build();

        Map<String, Object> putVideoInfo = new HashMap<>(); // ??????????????? - ????????????
        putVideoInfo.put("mberNo", tempMberNo);
        putVideoInfo.put("estblCoursCd", estblCoursCd);

        ScoreDTO scoreDTO = ScoreDTO.builder()
                .screSn(0)
                .atendScore(0)
                .tasksCore(0)
                .middleExprScore(0)
                .trmendExprScore(0)
                .estblCoursCd(estblCoursCd)
                .mberNo(tempMberNo)
                .build();

        /** ?????? ?????? ?????? ***************************************************************************************/
        if(scoreMapper.checkStudentScore(scoreDTO) == null) {
            scoreMapper.insertStudentScore(scoreDTO);
        }

        /** ????????? ?????? ?????? ?????? - ???????????? ?????? ?????? ??? */
        List<AtendDTO> atendList = null; // ????????????
        List<OnlineLecForPrintDTO> onlineLecForVideoInfo = getOnlineLectureList(atnlcLctreDTO);
        logger.info("onlineLecForVideoInfo : " + onlineLecForVideoInfo.toString());
        for(OnlineLecForPrintDTO onlineLecForPrintDTOTest : onlineLecForVideoInfo) {

            /** ????????? ?????? ?????? ?????? */
            String onlineLecCd = onlineLecForPrintDTOTest.getOnlineLecCd();
            logger.debug("goOnlineLecture{} - onlineLecCd ::: {}", onlineLecCd, onlineLecCd);

            /** ???????????? ??? ????????? ??????
             *  ??? ????????? ?????? ??????  : getVidoInfo(putVideoInfo)
             *  ??? ???????????? ??????    : getAtendInfo(putVideoInfo);
             * */
            putVideoInfo.put("onlineLecCd", onlineLecCd); // ????????? ?????? ??????
            vidoInfoDTO = getVidoInfo(putVideoInfo);
            atendList = getAtendInfo(putVideoInfo);

            /** null??? ?????? ???????????? ?????? */
            if(atendList==null || atendList.isEmpty()) {
                insertAtendInfo(putVideoInfo);
            }

            /** ????????? ?????? ?????? ?????? ??????(????????? ???) */
            if(vidoInfoDTO == null) {
                vidoInfoDTO = VidoInfoDTO.builder()
                        .vidoInfoSn(0)
                        .mberNo(tempMberNo)
                        .vidoPlaytime(0)
                        .vidoProgress(0)
                        .onlineLecCd(onlineLecCd)
                        .build();
                /** ????????? ?????? ?????? */
                logger.debug("goOnlineLecture{} - ????????? ?????? ??????");
                insertVidoInfo = saveOnlineLecVideoInfo(vidoInfoDTO);
            }
        }

        /** ????????? ?????? ?????? ?????? */
        List<OnlineLecForPrintDTO> onlineLecPrintList = getOnlineLectureListVer2(atnlcLctreDTO);
        onlineLecPrintList = onlineLecPrintList.stream().sorted(Comparator.comparing(OnlineLecForPrintDTO::getOnlineLecWeek)).collect(Collectors.toList());
        logger.info("goOnlineLecture{} - onlineLecListMap Version 2 : {}", insertVidoInfo, onlineLecPrintList.toString());

        /** ?????? ?????? ?????? ??? ???????????? ?????? - (??????, ????????????, ????????????) ?????? */
        for(OnlineLecForPrintDTO onlineLecForPrintDTO : onlineLecPrintList) {
            AtendDTO atendDTO = null;
            try {
                Date startLectureDay = sdf.parse(onlineLecForPrintDTO.getOnlineLecStart().toString());
                Date endLectureDay = sdf.parse(onlineLecForPrintDTO.getOnlineLecEnd().toString());
                Date nowDate = dateFormat.parse(now.toString());
                int videoProgress = onlineLecForPrintDTO.getVidoProgress();

                if(startLectureDay.before(nowDate) && endLectureDay.after(nowDate)) {
                    logger.info("????????????");
                    onlineLecForPrintDTO.setLearningStatus("????????????");
                } else if(startLectureDay.after(nowDate)) {
                    logger.info("????????????");
                    onlineLecForPrintDTO.setLearningStatus("????????????");
                } else if(endLectureDay.before(nowDate)) {
                    logger.info("????????????");
                    onlineLecForPrintDTO.setLearningStatus("????????????");

                    if (videoProgress < 50) {
                        atendDTO =
                                new AtendDTO("n", nowDate, tempMberNo, onlineLecForPrintDTO.getOnlineLecCd());
                        updateAtendForOnlineLecture(atendDTO);
                    } else if(videoProgress >= 50 && videoProgress <= 95) {
                        atendDTO =
                                new AtendDTO("o", nowDate, tempMberNo, onlineLecForPrintDTO.getOnlineLecCd());
                        updateAtendForOnlineLecture(atendDTO);
                    }
                }
                int onlineLecLearningStatus = updateOnlineLecLearningStatus(onlineLecForPrintDTO);
            } catch (ParseException e) {
                logger.error("{}", e);
            } catch (Exception e) {
                logger.error("{}", e);
            }
        }

        /** ???????????? */
        paramMap.put("onlineLecPrintList", onlineLecPrintList);
    }

    @Override
    public void checkAtendInfo(Map<String, Object> paramMap) {
        int mberNo = Integer.parseInt(paramMap.get("tempMberNo").toString());
        String estblCoursCd = paramMap.get("estblCoursCd").toString();
        AtendDTO atendDTO = AtendDTO.builder()
                .mberNo(mberNo)
                .estblCoursCd(estblCoursCd)
                .build();
        ScoreDTO scoreDTO = ScoreDTO.builder()
                .mberNo(mberNo)
                .estblCoursCd(estblCoursCd)
                .build();

        List<AtendDTO> atendList = scoreMapper.checkAtendInfo(atendDTO);
        ScoreDTO scoreInfo = scoreMapper.checkStudentScore(scoreDTO);
        int atendScore = 0;

        for (AtendDTO atend : atendList) {
            String atendStus = atend.getAtendSttus();

            if(atendStus.equals("y")) {
                atendScore += 6;
            } else if(atendStus.equals("n")) {
                //????????? ?????? ????????? ??????.
            } else if(atendStus.equals("o")) {
                atendScore += 3;
            }
        }
        logger.info("scoreInfo L: " + scoreInfo);
        scoreInfo.setAtendScore(atendScore);
        logger.info("scoreInfo E: " + scoreInfo);
        scoreMapper.updateScoreForOther(scoreInfo);
    }
}
