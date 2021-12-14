package com.example.dditlms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstblCoursDTO {
    private String estblCoursCd;
    private int atchmnflId;
    private int lctreNmpr;
    private String lctreSe;
    private String lctreKnd;
    private String lctreStut;
    private String sbjectCd;
    private String majorCode;
    private String yearSeme;
    private String lctrumCd;
    private int mberNo;

    public EstblCoursDTO(String estblCoursCd, int mberNo) {
        this.estblCoursCd = estblCoursCd;
        this.mberNo = mberNo;
    }
}
