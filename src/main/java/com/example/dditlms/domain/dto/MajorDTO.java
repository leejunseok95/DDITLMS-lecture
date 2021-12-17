package com.example.dditlms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MajorDTO {
    private String majorCode;
    private String majorSe;
    private String majorNm;
    private String parMajorCode;
    private int registAmount;
}
