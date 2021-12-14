package com.example.dditlms.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VidoInfoDTO {
    private int vidoInfoSn;
    private int mberNo;
    private int vidoPlaytime;
    private int vidoProgress;
    private String onlineLecCd;

    @Override
    public String toString() {
        return "VidoInfoDTO{" +
                "vidoInfoSn=" + vidoInfoSn +
                ", mberNo=" + mberNo +
                ", vidoPlaytime='" + vidoPlaytime + '\'' +
                ", vidoProgress='" + vidoProgress + '\'' +
                ", onlineLecCd='" + onlineLecCd + '\'' +
                '}';
    }
}
