package com.example.dditlms.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MberDTO {
    private long mberNo;
    private String email;
    private int failCount;
    private String memberId;
    private String memberImg;
    private String mberNm;
    private String menuOd;
    private String password;
    private String telno;
    private String mberSe;

    @Override
    public String toString() {
        return "MberDTO{" +
                "mberNo=" + mberNo +
                ", email='" + email + '\'' +
                ", failCount=" + failCount +
                ", memberId='" + memberId + '\'' +
                ", memberImg='" + memberImg + '\'' +
                ", mberNm='" + mberNm + '\'' +
                ", menuOd='" + menuOd + '\'' +
                ", password='" + password + '\'' +
                ", telno='" + telno + '\'' +
                ", mberSe='" + mberSe + '\'' +
                '}';
    }
}
