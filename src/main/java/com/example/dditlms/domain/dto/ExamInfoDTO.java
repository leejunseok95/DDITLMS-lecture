package com.example.dditlms.domain.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamInfoDTO {
    private String examInfoCd;
    private String examInfoTitle;
    private String examInfoCategory;
    private Date examInfoDate;
    private String examInfoContent;
    private String estblCoursCd;
    private int examInfoTimelimit;

    @Override
    public String toString() {
        return "ExamInfoDTO{" +
                "examInfoCd='" + examInfoCd + '\'' +
                ", examInfoTitle='" + examInfoTitle + '\'' +
                ", examInfoCategory='" + examInfoCategory + '\'' +
                ", examInfoDate='" + examInfoDate + '\'' +
                ", examInfoContent='" + examInfoContent + '\'' +
                ", estblCoursCd='" + estblCoursCd + '\'' +
                ", examInfoTimelimit=" + examInfoTimelimit +
                '}';
    }
}
