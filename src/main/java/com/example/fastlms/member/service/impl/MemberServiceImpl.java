package com.example.fastlms.member.service.impl;

import com.example.fastlms.components.MailComponents;
import com.example.fastlms.member.entity.Member;
import com.example.fastlms.member.exception.MemberNotEmailAuthException;
import com.example.fastlms.member.model.MemberInput;
import com.example.fastlms.member.repository.MemberRepository;
import com.example.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    // 회원가입
    @Override
    public boolean register(MemberInput parameter) {


        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if (optionalMember.isPresent()){
            return false;
        }

        String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .password(encPassword)
                .phone(parameter.getPhone())
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthDt(LocalDateTime.now())
                .emailAuthKey(uuid)
                .build();


        memberRepository.save(member);


        String email = parameter.getUserId();
        String subject = "fastlms 사이트 가입을 축하드립니다. ";
        String text = "<p>fastlms 사이트 가입을 축하드립니다. </p><p> 아래 링크를 클릭하셔서 가입을 완료 하세요. </p>"
                + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid + "'>가입 완료 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }
        Member member = optionalMember.get();
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다 .");
        }
        Member member = optionalMember.get();

        if (!member.isEmailAuthYn()) {
            try {
                throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인을 해주세요.");
            } catch (MemberNotEmailAuthException e) {
                throw new RuntimeException(e);
            }
        }


        List<GrantedAuthority> grantedAuthorities  = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}
