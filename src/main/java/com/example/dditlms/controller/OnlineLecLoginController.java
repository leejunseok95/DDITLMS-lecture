package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.MberDTO;
import com.example.dditlms.mapper.MberMapper;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.security.JwtSecurityService;
import com.example.dditlms.security.Role;
import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Model;
import org.bouncycastle.math.raw.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OnlineLecLoginController {
    private static final Logger logger = LoggerFactory.getLogger(OnlineLecLoginController.class);

    private final JwtSecurityService jwtSecurityService;
    private final SessionRegistry sessionRegistry;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final SecurityContextRepository securityContextRepository;
    private final MberMapper mapper;

    @GetMapping("/onlineLecture/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        @RequestParam(value = "cnt", required = false) String cnt,
                        Model model) {

        return "pages/onlineLectureLogin";
    }

    @GetMapping("/")
    public String checkUserTogoMainPage(HttpSession session) {
        MberDTO member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
            session.setAttribute("mberNm", member.getMberNm());
        }catch(ClassCastException e){
            return "redirect:/onlineLecture/login";
        }
        String selection = member.getMberSe();
        logger.info("OnlineLecLogincontroller - checkUserTogoMainPage - selection : {}", selection);
        if(selection.equals("ROLE_STUDENT")){
            return "redirect:/student/online/studentMain";
//        }else if(selection.equals("ROLE_PROFESSOR")){
        }else if(selection.equals("ROLE_PROFESSOR")){
            return "redirect:/professor/online/professorMain";
        } else {
            return "redirect:/onlineLecture/login";
        }
    }

    @GetMapping("/getSsotoken")
    public String test(ModelAndView mv, HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam String token) {
        String id = jwtSecurityService.getToken(token);
        MberDTO member = mapper.onlineLecLoginById(id);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.valueOf(member.getMberSe()).getValue()));
        AccountContext accountContext = new AccountContext(member,authorities);

        Authentication userNameToken = new UsernamePasswordAuthenticationToken(accountContext,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(userNameToken);

        sessionAuthenticationStrategy.onAuthentication(userNameToken,request,response);
        sessionRegistry.registerNewSession(request.getSession().getId(),userNameToken.getPrincipal());
        securityContextRepository.saveContext(SecurityContextHolder.getContext(),request,response);

        return "redirect:/";
    }

//    @GetMapping("/loginTest")
//    public String loginTest(HttpServletRequest request
//            , HttpServletResponse response){
////        Optional<MberDTO> memberEntityWrapper = memberRepository.findById(2014161091L);
//        Optional<MberDTO> memberEntityWrapper = mapper.onlineLecLoginById();
//        MberDTO member = memberEntityWrapper.orElse(null);
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(Role.valueOf(member.getMberSe()).getValue()));
//        AccountContext accountContext = new AccountContext(member,authorities);
//
//        Authentication token = new UsernamePasswordAuthenticationToken(accountContext,null,authorities);
//        SecurityContextHolder.getContext().setAuthentication(token);
//
//        sessionAuthenticationStrategy.onAuthentication(token,request,response);
//        sessionRegistry.registerNewSession(request.getSession().getId(),token.getPrincipal());
//        securityContextRepository.saveContext(SecurityContextHolder.getContext(),request,response);
//        return "redirect:/";
//    }
}
