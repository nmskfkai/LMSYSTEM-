package com.example.lms.member.service.impl;

import com.example.lms.member.entity.Member;
import com.example.lms.member.repository.MemberRepository;
import com.example.lms.member.service.KakaoService;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;


@Service
public class KakaoServiceImpl implements KakaoService{

    @Autowired
    MemberRepository memberRepository;
    @Override
    public String getToken(String code) throws Exception {
        String access_Token = "";

        //EndPoint URL = API가 서버에서 자원에 접근할 수 있도록 하는 URL
        final String requestUrl = "https://kauth.kakao.com/oauth/token";

        //토큰을 요청할 URL 객체 생성
        URL url = new URL(requestUrl);

        //HTTP 연결 설정
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        //서버로 요청 보내기
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id=37c93ea0bb15b3614a2375487d27eea1");
        sb.append("&client_secret=7BbE7NqzuF4FbLfBODfkCzd31ENyQ7aF");
        sb.append("&redirect_uri=http://lmsproject.ddns.net:8080/auth/kakao/callback");
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();

        //서버의 응답 데이터 가져옴
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line = "";
        String result = "";

        //result에 토큰이 포함된 응답데이터를 한줄씩 저장
        while ((line = br.readLine()) != null) {
            result += line;
        }

        //JSON 데이터를 파싱하기 위한 JsonParser
        JsonParser parser = new JsonParser();
        //값 추출을 위해 파싱한 데이터를 JsonElement로 변환
        JsonElement element = parser.parse(result);

        //element에서 access_token 값을 얻어옴
        access_Token = element.getAsJsonObject().get("access_token").getAsString();

        br.close();
        bw.close();

        return access_Token;

    }

    @Override
    public ArrayList<Object> getUserInfo(String access_token) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();

        final String requestUrl = "https://kapi.kakao.com/v2/user/me";

        URL url = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + access_token);

        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line = "";
        String result = "";

        while ((line = bf.readLine()) != null) {
            result +=line;
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

        String thumbnail_image = null;
        String ninkname = null;
        String email = null;
        String gender = null;
        String birthday = null;

        JsonObject properties = element.getAsJsonObject().getAsJsonObject("properties");
        JsonObject kakao_account = element.getAsJsonObject().getAsJsonObject("kakao_account");

        if (properties != null) {
            JsonElement thumbnailImageElement = properties.get("thumbnail_image");
            if (thumbnailImageElement != null && !thumbnailImageElement.isJsonNull()) {
                thumbnail_image = thumbnailImageElement.getAsString();
            }

            JsonElement nicknameElement = properties.get("nickname");
            if (nicknameElement != null && !nicknameElement.isJsonNull()) {
                ninkname = nicknameElement.getAsString();
            }
        }

        if (kakao_account != null) {
            JsonElement emailElement = kakao_account.get("email");
            if (emailElement != null && !emailElement.isJsonNull()) {
                email = emailElement.getAsString();
            }

            JsonElement genderElement = kakao_account.get("gender");
            if (genderElement != null && !genderElement.isJsonNull()) {
                gender = genderElement.getAsString();
            }

            JsonElement birthdayElement = kakao_account.get("birthday");
            if (birthdayElement != null && !birthdayElement.isJsonNull()) {
                birthday = birthdayElement.getAsString();
            }
        }

        list.add(thumbnail_image);
        list.add(ninkname);
        list.add(email);
        list.add(birthday);

        String encPassword = BCrypt.hashpw("1234", BCrypt.gensalt());


        //DB 저장
        Member member = Member.builder()
                .userId(email) // 예시로 userId에 닉네임을 저장하도록 함
                .userName(ninkname)
                .phone("01011111111")
                .password(encPassword) // 예시로 고정된 비밀번호를 저장하도록 함
                .regDt(LocalDateTime.now())
                .emailAuthYn(true)
                .userStatus(Member.MEMBER_STATUS_ING)
                .build();
        memberRepository.save(member);


        return list;
    }


}
