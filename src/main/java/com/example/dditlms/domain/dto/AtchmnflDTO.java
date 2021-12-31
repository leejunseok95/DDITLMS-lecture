package com.example.dditlms.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtchmnflDTO {
    private int atchmnflSn;
    private int fileSn;
    private String fileStreCours;
    private String streFileNm;
    private String orignlFileNm;
    private String fileExtsn;
    private long fileSize;
    private String jobSe;
    private int dwldCo;

    @Override
    public String toString() {
        return "AtchmnflDTO{" +
                "atchmnflSn=" + atchmnflSn +
                ", fileSn=" + fileSn +
                ", fileStreCours='" + fileStreCours + '\'' +
                ", streFileNm='" + streFileNm + '\'' +
                ", orignlFileNm='" + orignlFileNm + '\'' +
                ", fileExtsn='" + fileExtsn + '\'' +
                ", fileSize=" + fileSize +
                ", jobSe='" + jobSe + '\'' +
                ", dwldCo=" + dwldCo +
                '}';
    }
}
