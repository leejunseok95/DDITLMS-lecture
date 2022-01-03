package com.example.dditlms.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtnlcLctreDTO {
    private int mberNo;
    private String estblCoursCd;
    private int pnt;
    private String histGrade;
    private String histMajorCode;
}
