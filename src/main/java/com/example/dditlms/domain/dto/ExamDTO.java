package com.example.dditlms.domain.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamDTO {
    private String examSn;
    private int examNumber;
    private String examTitle;
    private String examAnswer;
    private String examType;
    private String examContent;
    private String examInfoCd;

    @Override
    public String toString() {
        return "ExamDTO{" +
                "examSn='" + examSn + '\'' +
                ", examNumber=" + examNumber +
                ", examTitle='" + examTitle + '\'' +
                ", examAnswer='" + examAnswer + '\'' +
                ", examType='" + examType + '\'' +
                ", examContent='" + examContent + '\'' +
                ", examInfoCd='" + examInfoCd + '\'' +
                '}';
    }
}
