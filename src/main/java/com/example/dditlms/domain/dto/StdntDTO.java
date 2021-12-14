package com.example.dditlms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StdntDTO {
    private int mberNo;
    private String majorCode;
    private String entschDe;
    private int grade;
    private String stdntSe;
}
