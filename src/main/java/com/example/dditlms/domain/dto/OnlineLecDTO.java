package com.example.dditlms.domain.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineLecDTO {
    private String onlineLecCd;
    private int onlineLecWeek;
    private String onlineLecTitle;
    private Date onlineLecStart;
    private Date onlineLecEnd;
    private String learningStatus;
    private int atchmnflSn;
    private String estblCoursCd;
    private String vidoEndtime;
    private String vidoStarttime;

    @Override
    public String toString() {
        return "OnlineLecDTO{" +
                "onlineLecCd='" + onlineLecCd + '\'' +
                ", onlineLecWeek='" + onlineLecWeek + '\'' +
                ", onlineLecTitle='" + onlineLecTitle + '\'' +
                ", onlineLecStart=" + onlineLecStart +
                ", onlineLecEnd=" + onlineLecEnd +
                ", learningStatus='" + learningStatus + '\'' +
                ", atchmnflSn=" + atchmnflSn +
                ", estblCoursCd='" + estblCoursCd + '\'' +
                ", vidoEndtime='" + vidoEndtime + '\'' +
                ", vidoStarttime='" + vidoStarttime + '\'' +
                '}';
    }
}
