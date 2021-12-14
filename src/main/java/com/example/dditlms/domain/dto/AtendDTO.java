package com.example.dditlms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtendDTO {
    private String atendCode;
    private String atendSttus;
    private Date atendDate;
    private String atendTime;
    private int mberNo;
    private String estblCoursCd;
    private String onlineLecCd;

    public AtendDTO(String atendSttus, Date atendDate, int mberNo, String onlineLecCd) {
        this.atendSttus = atendSttus;
        this.atendDate = atendDate;
        this.mberNo = mberNo;
        this.onlineLecCd = onlineLecCd;
    }
}
