package com.example.dditlms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultDTO {
    private int examResultSn;
    private String examInput;
    private int mberNo;
    private int examNumber;
    private String examSn;
}
