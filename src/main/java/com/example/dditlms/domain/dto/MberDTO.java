package com.example.dditlms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MberDTO {
    private int mberNo;
    private String mberSe;
    private String mberNm;
    private String email;
    private String telno;
    private String bankNm;
    private String acnutNo;
    private String id;
    private String password;
}
