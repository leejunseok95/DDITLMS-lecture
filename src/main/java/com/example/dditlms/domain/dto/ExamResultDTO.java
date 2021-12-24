package com.example.dditlms.domain.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResultDTO {
    private int examResultSn;
    private String examInput;
    private int mberNo;
    private int examNumber;
    private String examSn;

    @Override
    public String toString() {
        return "ExamResultDTO{" +
                "examResultSn=" + examResultSn +
                ", examInput='" + examInput + '\'' +
                ", mberNo=" + mberNo +
                ", examNumber=" + examNumber +
                ", examSn='" + examSn + '\'' +
                '}';
    }
}
