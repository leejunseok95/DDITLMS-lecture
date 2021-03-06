package com.example.dditlms.security;

import com.example.dditlms.domain.dto.MberDTO;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.mapper.MberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

//    private final MemberRepository memberRepository;
    private final MberMapper mapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        MberDTO member = ((AccountContext)authentication.getPrincipal()).getMember();

        if(member.getFailCount()>=5){
            throw new LockedException("Account locked");
        }else{
            member.setFailCount(0);
            mapper.updateMberFailCount(member);
        }

        // ?????? ???????????? ????????? ????????? ?????? ??? ?????? ????????? ?????? ??????
        setDefaultTargetUrl("/");
        // ?????? ?????? ??????, Member ?????? ??????
        HttpSession session = request.getSession();
        if(session!=null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        session.setAttribute("memberImg",member.getMemberImg());


        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if(savedRequest != null){
            // ?????? ?????? ??? url??? ????????????
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request,response,targetUrl);
        }else{
            // ?????? url??? ????????? ??????
            redirectStrategy.sendRedirect(request,response,getDefaultTargetUrl());
        }
    }
}
