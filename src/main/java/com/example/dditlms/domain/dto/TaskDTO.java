package com.example.dditlms.domain.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private int taskSn;
    private String taskNm;
    private String taskCn;
    private Date taskPresentnTmlmt;
    private Date taskDe;
    private int atchmnflId;
    private String estblCoursCd;

    @Override
    public String toString() {
        return "TaskDTO{" +
                "taskSn=" + taskSn +
                ", taskNm='" + taskNm + '\'' +
                ", taskCn='" + taskCn + '\'' +
                ", taskPresentnTmlmt=" + taskPresentnTmlmt +
                ", taskDe=" + taskDe +
                ", atchmnflId=" + atchmnflId +
                ", estblCoursCd='" + estblCoursCd + '\'' +
                '}';
    }
}
