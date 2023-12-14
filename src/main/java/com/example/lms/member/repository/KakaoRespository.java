package com.example.lms.member.repository;

import com.example.lms.member.entity.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoRespository extends JpaRepository<KakaoUser, String> {
}
