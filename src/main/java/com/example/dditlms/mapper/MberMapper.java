package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MberMapper {
    MberDTO onlineLecLoginByMberNo(long mberNo);
    MberDTO onlineLecLoginById(String memberId);
    int updateMberFailCount(MberDTO mberDTO);
}