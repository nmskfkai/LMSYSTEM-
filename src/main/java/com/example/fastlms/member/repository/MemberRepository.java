package com.example.fastlms.member.repository;

import com.example.fastlms.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmailAuthKey(String emailAuthKey);
    Optional<Member> findByUserIdAndUserName(String userId, String username);
    Optional<Member> findByResetPasswordKey(String resetPasswordKey);


}
