package com.example.dditlms.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreDTO {
    private int screSn;
    private int atendScore;
    private int tasksCore;
    private int middleExprScore;
    private int trmendExprScore;
    private String estblCoursCd;
    private int mberNo;

    @Override
    public String toString() {
        return "ScoreDTO{" +
                "screSn=" + screSn +
                ", atendScore=" + atendScore +
                ", tasksCore=" + tasksCore +
                ", middleExprScore=" + middleExprScore +
                ", trmendExprScore=" + trmendExprScore +
                ", estblCoursCd='" + estblCoursCd + '\'' +
                ", mberNo=" + mberNo +
                '}';
    }
}
