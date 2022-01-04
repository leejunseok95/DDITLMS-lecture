package com.example.dditlms.service.impl;

import com.example.dditlms.controller.MemberForm;
import com.example.dditlms.domain.dto.MberDTO;
import com.example.dditlms.domain.entity.Identification;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.IdentificationRepository;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.mapper.MberMapper;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.security.Role;
import com.example.dditlms.service.MemberService;
import com.querydsl.core.support.QueryMixin;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

//    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final MberMapper mapper;
    private final IdentificationRepository identificationRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        long userNumber;
        logger.info("MemberServiceImpl - loadUserByUsername - account : {}", account);
//        Optional<Member> memberEntityWrapper;
        MberDTO mberDTO = null;
        try{
            userNumber = Long.parseLong(account);
//            logger.info("loadUserBysuernaem : {}", userNumber);
            //회원 조회하는 코드
//            memberEntityWrapper = memberRepository.findByUserNumber(userNumber);
            mberDTO = mapper.onlineLecLoginByMberNo(userNumber);
        }catch (NumberFormatException e){
            mberDTO = mapper.onlineLecLoginById(account);
        }

        logger.info("MemberServiceImpl - loadUserByUsername - mberDTO : {}", mberDTO.toString());

        if(mberDTO == null){
            return null;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.valueOf(mberDTO.getMberSe()).getValue()));

        AccountContext accountContext = new AccountContext(mberDTO,authorities);

        logger.info("AccountContext : {}", accountContext);

        return accountContext;
    }
}
