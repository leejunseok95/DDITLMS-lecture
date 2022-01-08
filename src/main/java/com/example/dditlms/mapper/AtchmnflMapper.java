package com.example.dditlms.mapper;

import com.example.dditlms.domain.dto.AtchmnflDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AtchmnflMapper {
    
    //file insert 메소드
    int uploadVideoFile(AtchmnflDTO atchmnflDTO);
    int deleteFile(String atchmnflSn);
    List<AtchmnflDTO> findByonlineLecCd(String onlineLecCd);
}
