package com.example.fastlms.member.service;

import com.example.fastlms.member.model.MemberInput;
import com.example.fastlms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {
    boolean register(MemberInput parameter);

    /**
     * uuid에 해당하는 계정을 활성화 함.
     */
    boolean emailAuth(String uuid);

    /**
     * 이메일로 비밀번호 초기화 정보를 전송.
     */
    boolean sendResetPassword(ResetPasswordInput resetPasswordInput);

    /**
     * 입력받은 uuid 에 대해서 비밀번호 초기화
     */
    boolean resetPassword(String uuid, String password);
    /**
     * 입력받은 uuid 가 유효한지 확인
     */
    boolean checkResetPassword(String uuid);
}
