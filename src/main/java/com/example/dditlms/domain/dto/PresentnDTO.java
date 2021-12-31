package com.example.dditlms.domain.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresentnDTO {
    private int mberNo;
    private int taskSn;
    private String presentnSj;
    private String presentnCn;
    private Date presentnDate;
    private int taskScore;
    private int atchmnflId;

    @Override
    public String toString() {
        return "PresentnDTO{" +
                "mberNo=" + mberNo +
                ", taskSn=" + taskSn +
                ", presentnSj='" + presentnSj + '\'' +
                ", presentnCn='" + presentnCn + '\'' +
                ", presentnDate=" + presentnDate +
                ", taskScore=" + taskScore +
                ", atchmnflId=" + atchmnflId +
                '}';
    }
}
